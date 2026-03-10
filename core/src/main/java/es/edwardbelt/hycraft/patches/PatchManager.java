package es.edwardbelt.hycraft.patches;

import es.edwardbelt.hycraft.patches.advice.*;
import es.edwardbelt.hycraft.patches.api.*;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassInjector;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Consumer;

public class PatchManager {
    private static final Set<ClassLoader> injectedLoaders = Collections.newSetFromMap(new WeakHashMap<>());

    public static void apply(Class<?> patchClass) {
        Patch patch = patchClass.getAnnotation(Patch.class);
        if (patch == null) {
            throw new IllegalArgumentException(
                    patchClass.getName() + " is not annotated with @Patch");
        }

        Class<?> target = patch.value();
        ClassLoader targetLoader = target.getClassLoader();

        ensureBridgeInjected(targetLoader);

        List<PatchEntry> entries = collectEntries(patchClass, target);
        if (entries.isEmpty()) {
            throw new IllegalArgumentException(
                    patchClass.getName() + " has no @Inject or @Overwrite methods");
        }

        for (PatchEntry entry : entries) {
            registerHandler(targetLoader, entry);
        }

        DynamicType.Builder<?> builder = new ByteBuddy().redefine(target);
        for (PatchEntry entry : entries) {
            builder = builder.visit(
                    Advice.to(entry.adviceClass)
                            .on(ElementMatchers.named(entry.targetMethodName))
            );
        }

        builder.make().load(targetLoader, ClassReloadingStrategy.fromInstalledAgent());
    }

    public static void clearAll(ClassLoader targetLoader) {
        try {
            Class<?> registry = Class.forName(PatchRegistry.class.getName(), true, targetLoader);
            registry.getMethod("clear").invoke(null);
        } catch (Exception ignored) {
        }
    }

    private static void ensureBridgeInjected(ClassLoader target) {
        if (injectedLoaders.contains(target)) return;

        Map<TypeDescription, byte[]> toInject = new HashMap<>();
        toInject.put(
                new TypeDescription.ForLoadedType(PatchRegistry.class),
                ClassFileLocator.ForClassLoader.read(PatchRegistry.class)
        );

        new ClassInjector.UsingReflection(target).inject(toInject);
        injectedLoaders.add(target);
    }

    private static List<PatchEntry> collectEntries(Class<?> patchClass,
                                                   Class<?> target) {
        List<PatchEntry> entries = new ArrayList<>();

        for (Method handler : patchClass.getDeclaredMethods()) {
            if (handler.isAnnotationPresent(Inject.class)) {
                entries.add(buildInjectEntry(handler, target));
            } else if (handler.isAnnotationPresent(Overwrite.class)) {
                entries.add(buildOverwriteEntry(handler, target));
            }
        }
        return entries;
    }

    private static PatchEntry buildInjectEntry(Method handler, Class<?> target) {
        validateHandler(handler);
        Inject inject = handler.getAnnotation(Inject.class);

        Method targetMethod = resolveTargetMethod(target, inject.method(), inject.args());
        String key = computeKey(target, targetMethod);
        boolean isVoid = targetMethod.getReturnType() == void.class;
        boolean isStatic = Modifier.isStatic(targetMethod.getModifiers());

        Class<?> adviceClass;
        if (inject.at() == At.HEAD) {
            if (inject.cancellable()) {
                adviceClass = pickAdvice(isVoid, isStatic,
                        CancellableVoidHeadAdvice.class,
                        CancellableVoidHeadInstanceAdvice.class,
                        CancellableHeadAdvice.class,
                        CancellableHeadInstanceAdvice.class);
            } else {
                adviceClass = isStatic ? HeadAdvice.class : HeadInstanceAdvice.class;
            }
        } else {
            if (isVoid) {
                adviceClass = isStatic ? VoidReturnAdvice.class : VoidReturnInstanceAdvice.class;
            } else {
                adviceClass = isStatic ? ReturnAdvice.class : ReturnInstanceAdvice.class;
            }
        }

        return new PatchEntry(key, inject.method(), handler, adviceClass);
    }

    private static PatchEntry buildOverwriteEntry(Method handler, Class<?> target) {
        validateHandler(handler);
        Overwrite ow = handler.getAnnotation(Overwrite.class);

        Method targetMethod = resolveTargetMethod(target, ow.method(), ow.args());
        String key = computeKey(target, targetMethod);
        boolean isVoid = targetMethod.getReturnType() == void.class;
        boolean isStatic = Modifier.isStatic(targetMethod.getModifiers());

        Class<?> adviceClass;
        if (isVoid) {
            adviceClass = isStatic ? OverwriteVoidAdvice.class : OverwriteVoidInstanceAdvice.class;
        } else {
            adviceClass = isStatic ? OverwriteAdvice.class : OverwriteInstanceAdvice.class;
        }

        return new PatchEntry(key, ow.method(), handler, adviceClass);
    }

    private static Class<?> pickAdvice(boolean isVoid, boolean isStatic,
                                       Class<?> voidStatic, Class<?> voidInstance,
                                       Class<?> nonVoidStatic, Class<?> nonVoidInstance) {
        if (isVoid) return isStatic ? voidStatic : voidInstance;
        return isStatic ? nonVoidStatic : nonVoidInstance;
    }

    private static void validateHandler(Method handler) {
        if (!Modifier.isStatic(handler.getModifiers())) {
            throw new IllegalArgumentException("Handler must be static: " + handler);
        }
        Class<?>[] params = handler.getParameterTypes();
        if (params.length != 1 || params[0] != CallbackInfo.class) {
            throw new IllegalArgumentException("Handler must accept exactly one CallbackInfo parameter: " + handler);
        }
    }

    private static Method resolveTargetMethod(Class<?> target,
                                              String methodName,
                                              Class<?>[] argTypes) {
        if (argTypes != null && argTypes.length > 0) {
            try {
                return target.getDeclaredMethod(methodName, argTypes);
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException("Method not found: " + target.getName() + "." + methodName + " with specified arg types", e);
            }
        }

        Method found = null;
        for (Method m : target.getDeclaredMethods()) {
            if (m.getName().equals(methodName)) {
                if (found != null) {
                    throw new IllegalArgumentException("Overloaded method '" + methodName + "' in " + target.getName() + " specify args to disambiguate");
                }
                found = m;
            }
        }
        if (found == null) {
            for (Class<?> cls = target.getSuperclass(); cls != null; cls = cls.getSuperclass()) {
                for (Method m : cls.getDeclaredMethods()) {
                    if (m.getName().equals(methodName)) {
                        if (found != null) {
                            throw new IllegalArgumentException("Overloaded method '" + methodName + "' specify args to disambiguate");
                        }
                        found = m;
                    }
                }
                if (found != null) break;
            }
        }
        if (found == null) {
            throw new IllegalArgumentException(
                    "Method not found: " + target.getName() + "." + methodName);
        }
        return found;
    }

    private static String computeKey(Class<?> target, Method method) {
        String descriptor = getMethodDescriptor(method);
        return target.getName() + "." + method.getName() + descriptor;
    }

    private static String getMethodDescriptor(Method method) {
        StringBuilder sb = new StringBuilder("(");
        for (Class<?> param : method.getParameterTypes()) {
            sb.append(getTypeDescriptor(param));
        }
        sb.append(")");
        sb.append(getTypeDescriptor(method.getReturnType()));
        return sb.toString();
    }

    private static String getTypeDescriptor(Class<?> type) {
        if (type == void.class) return "V";
        if (type == boolean.class) return "Z";
        if (type == byte.class) return "B";
        if (type == char.class) return "C";
        if (type == short.class) return "S";
        if (type == int.class) return "I";
        if (type == long.class) return "J";
        if (type == float.class) return "F";
        if (type == double.class) return "D";
        if (type.isArray()) {
            return "[" + getTypeDescriptor(type.getComponentType());
        }
        return "L" + type.getName().replace('.', '/') + ";";
    }

    private static void registerHandler(ClassLoader targetLoader, PatchEntry entry) {
        try {
            Class<?> registryInTarget = Class.forName(
                    PatchRegistry.class.getName(), true, targetLoader);
            Method registerMethod = registryInTarget.getMethod(
                    "register", String.class, Consumer.class);

            Method handler = entry.handler;
            handler.setAccessible(true);

            Consumer<Object[]> bridge = state -> {
                try {
                    CallbackInfo ci = new CallbackInfo(state);
                    handler.invoke(null, ci);
                } catch (Exception e) {
                    throw new RuntimeException("Patch handler failed: " + handler, e);
                }
            };

            registerMethod.invoke(null, entry.key, bridge);
        } catch (Exception e) {
            throw new RuntimeException("Failed to register handler for " + entry.key, e);
        }
    }

    private static class PatchEntry {
        final String key;
        final String targetMethodName;
        final Method handler;
        final Class<?> adviceClass;

        PatchEntry(String key, String targetMethodName,
                   Method handler, Class<?> adviceClass) {
            this.key = key;
            this.targetMethodName = targetMethodName;
            this.handler = handler;
            this.adviceClass = adviceClass;
        }
    }
}
