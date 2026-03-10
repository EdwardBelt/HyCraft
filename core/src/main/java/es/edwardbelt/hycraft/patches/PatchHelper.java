package es.edwardbelt.hycraft.patches;

import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.loading.ClassInjector;
import net.bytebuddy.jar.asm.*;

import java.lang.instrument.Instrumentation;
import java.util.*;

public class PatchHelper {
    private static Instrumentation instrumentation;

    public static void init() {
        ByteBuddyAgent.install();
        instrumentation = ByteBuddyAgent.getInstrumentation();
    }

    public static void injectAdvice(ClassLoader target,
                                    Class<?> adviceClass,
                                    String... packages) {
        Set<Class<?>> toInject = new HashSet<>();
        collectDependencies(adviceClass, packages, toInject);
        inject(target, toInject);
    }

    private static void collectDependencies(Class<?> root,
                                            String[] packages,
                                            Set<Class<?>> collected) {
        if (root == null || !collected.add(root)) return;

        ClassReader reader = new ClassReader(
                ClassFileLocator.ForClassLoader.read(root)
        );

        Set<String> referenced = new HashSet<>();
        reader.accept(new ClassVisitor(Opcodes.ASM9) {
            @Override
            public void visit(int version, int access, String name,
                              String signature, String superName,
                              String[] interfaces) {
                if (superName != null) referenced.add(superName);
                if (interfaces != null) {
                    Collections.addAll(referenced, interfaces);
                }
                super.visit(version, access, name, signature,
                        superName, interfaces);
            }

            @Override
            public FieldVisitor visitField(int access, String name,
                                           String descriptor,
                                           String signature,
                                           Object value) {
                collectFromDescriptor(descriptor, referenced);
                return super.visitField(access, name, descriptor,
                        signature, value);
            }

            @Override
            public MethodVisitor visitMethod(int access, String name,
                                             String descriptor,
                                             String signature,
                                             String[] exceptions) {
                collectFromDescriptor(descriptor, referenced);
                return new MethodVisitor(Opcodes.ASM9,
                        super.visitMethod(access, name, descriptor,
                                signature, exceptions)) {
                    @Override
                    public void visitTypeInsn(int opcode, String type) {
                        referenced.add(type);
                        super.visitTypeInsn(opcode, type);
                    }

                    @Override
                    public void visitFieldInsn(int opcode, String owner,
                                               String name, String desc) {
                        referenced.add(owner);
                        collectFromDescriptor(desc, referenced);
                        super.visitFieldInsn(opcode, owner, name, desc);
                    }

                    @Override
                    public void visitMethodInsn(int opcode, String owner,
                                                String name, String desc,
                                                boolean isInterface) {
                        referenced.add(owner);
                        collectFromDescriptor(desc, referenced);
                        super.visitMethodInsn(opcode, owner, name,
                                desc, isInterface);
                    }
                };
            }
        }, ClassReader.SKIP_FRAMES);

        for (String ref : referenced) {
            String className = ref.replace('/', '.');
            if (matchesAny(className, packages)) {
                try {
                    Class<?> cls = Class.forName(className, false,
                            root.getClassLoader());
                    collectDependencies(cls, packages, collected);
                } catch (ClassNotFoundException ignored) {}
            }
        }

    }

    private static boolean matchesAny(String className, String[] packages) {
        for (String pkg : packages) {
            if (className.startsWith(pkg)) return true;
        }
        return false;
    }

    private static void collectFromDescriptor(String desc,
                                              Set<String> out) {
        Type type = Type.getType(desc);
        collectFromType(type, out);
    }

    private static void collectFromType(Type type,
                                        Set<String> out) {
        if (type.getSort() == Type.OBJECT) {
            out.add(type.getInternalName());
        } else if (type.getSort() == Type.ARRAY) {
            collectFromType(type.getElementType(), out);
        } else if (type.getSort() == Type.METHOD) {
            for (Type arg : type.getArgumentTypes()) {
                collectFromType(arg, out);
            }
            collectFromType(type.getReturnType(), out);
        }
    }

    private static void inject(ClassLoader target, Set<Class<?>> classes) {
        Map<TypeDescription, byte[]> map = new HashMap<>();
        for (Class<?> cls : classes) {
            map.put(
                    new TypeDescription.ForLoadedType(cls),
                    ClassFileLocator.ForClassLoader.read(cls)
            );
        }
        new ClassInjector.UsingReflection(target).inject(map);
    }
}
