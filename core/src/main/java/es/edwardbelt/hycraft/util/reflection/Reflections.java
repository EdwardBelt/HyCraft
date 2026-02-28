package es.edwardbelt.hycraft.util.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Reflections {

    /**
     * Retrieve a field accessor for a specific field name only.
     *
     * @param target - the target type.
     * @param name - the name of the field.
     * @return The field accessor.
     */
    public static FieldAccessor<Object> getField(Class<?> target, String name) {
        return getFieldByName(target, name);
    }


    /**
     * Retrieve a field accessor for a specific field type specified by class name.
     *
     * @param target - the target type.
     * @param searchInnerClasses - whether to search for inner classes of the target class.
     * @param fieldTypeClassName - the name of the field type class.
     * @return The field accessor.
     */
    public static <T> FieldAccessor<T> getField(Class<?> target, boolean searchInnerClasses, String fieldTypeClassName) {
        return getField(target, null, searchInnerClasses, fieldTypeClassName, 0);
    }

    /**
     * Retrieve a field accessor for a specific field type specified by class name.
     *
     * @param target - the target type.
     * @param fieldName - the name of the field, or NULL to ignore.
     * @param searchInnerClasses - whether to search for inner classes of the target class.
     * @param fieldTypeClassName - the name of the field type class.
     * @return The field accessor.
     */
    public static <T> FieldAccessor<T> getField(Class<?> target, String fieldName, boolean searchInnerClasses, String fieldTypeClassName) {
        return getField(target, fieldName, searchInnerClasses, fieldTypeClassName, 0);
    }

    /**
     * Retrieve a field accessor for a specific field type specified by class name.
     *
     * @param target - the target type.
     * @param fieldName - the name of the field, or NULL to ignore.
     * @param searchInnerClasses - whether to search for inner classes of the target class.
     * @param fieldTypeClassName - the name of the field type class.
     * @param index - the number of compatible fields to skip.
     * @return The field accessor.
     */
    public static <T> FieldAccessor<T> getField(Class<?> target, String fieldName, boolean searchInnerClasses, String fieldTypeClassName, int index) {
        Class<?> fieldTypeClass = getClass(fieldTypeClassName);

        if (fieldTypeClass == null && searchInnerClasses) {
            fieldTypeClass = findInnerClass(target, fieldTypeClassName);
        }

        if (fieldTypeClass == null) {
            throw new IllegalArgumentException("Cannot find class with name: " + fieldTypeClassName);
        }

        @SuppressWarnings("unchecked")
        Class<T> typedFieldClass = (Class<T>) fieldTypeClass;

        return getField(target, fieldName, typedFieldClass, index);
    }

    /**
     * Helper method to find inner classes by name.
     *
     * @param parentClass - the parent class to search in.
     * @param innerClassName - the simple name of the inner class.
     * @return The inner class, or null if not found.
     */
    private static Class<?> findInnerClass(Class<?> parentClass, String innerClassName) {
        for (Class<?> innerClass : parentClass.getDeclaredClasses()) {
            if (innerClass.getSimpleName().equals(innerClassName)) {
                return innerClass;
            }
        }

        String fullInnerClassName = parentClass.getName() + "$" + innerClassName;
        return getClass(fullInnerClassName);
    }

    /**
     * Retrieve a field accessor for a specific field name only.
     *
     * @param className - lookup name of the class, see {@link #getClass(String)}.
     * @param name - the name of the field.
     * @return The field accessor.
     */
    public static FieldAccessor<Object> getField(String className, String name) {
        return getFieldByName(getClass(className), name);
    }

    /**
     * Retrieve a field accessor for a specific field type and name.
     *
     * @param target - the target type.
     * @param name - the name of the field, or NULL to ignore.
     * @param fieldType - a compatible field type.
     * @return The field accessor.
     */
    public static <T> FieldAccessor<T> getField(Class<?> target, String name, Class<T> fieldType) {
        return getField(target, name, fieldType, 0);
    }

    /**
     * Retrieve a field accessor for a specific field type and name.
     *
     * @param className - lookup name of the class, see {@link #getClass(String)}.
     * @param name - the name of the field, or NULL to ignore.
     * @param fieldType - a compatible field type.
     * @return The field accessor.
     */
    public static <T> FieldAccessor<T> getField(String className, String name, Class<T> fieldType) {
        return getField(getClass(className), name, fieldType, 0);
    }

    /**
     * Retrieve a field accessor for a specific field type and name.
     *
     * @param target - the target type.
     * @param fieldType - a compatible field type.
     * @return The field accessor.
     */
    public static <T> FieldAccessor<T> getField(Class<?> target, Class<T> fieldType) {
        return getField(target, null, fieldType, 0);
    }

    /**
     * Retrieve a field accessor for a specific field type and name.
     *
     * @param target - the target type.
     * @param fieldType - a compatible field type.
     * @param index - the number of compatible fields to skip.
     * @return The field accessor.
     */
    public static <T> FieldAccessor<T> getField(Class<?> target, Class<T> fieldType, int index) {
        return getField(target, null, fieldType, index);
    }

    public static <T> FieldAccessor<T> getField(String className, Class<T> fieldType) {
        return getField(className, fieldType, 0);
    }

    /**
     * Retrieve a field accessor for a specific field type and name.
     *
     * @param className - lookup name of the class, see {@link #getClass(String)}.
     * @param fieldType - a compatible field type.
     * @param index - the number of compatible fields to skip.
     * @return The field accessor.
     */
    public static <T> FieldAccessor<T> getField(String className, Class<T> fieldType, int index) {
        return getField(getClass(className), fieldType, index);
    }

    private static FieldAccessor<Object> getFieldByName(final Class<?> target, final String name) {
        for (final Field field : target.getDeclaredFields()) {
            if (field.getName().equals(name)) {
                field.setAccessible(true);

                return new FieldAccessor<Object>() {

                    @Override
                    public Field getField() {
                        return field;
                    }

                    @Override
                    public Class getFieldClass() {
                        return field.getType();
                    }

                    @Override
                    public Object get(Object target) {
                        try {
                            return field.get(target);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("Cannot access reflection.", e);
                        }
                    }

                    @Override
                    public void set(Object target, Object value) {
                        try {
                            field.set(target, value);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("Cannot access reflection.", e);
                        }
                    }

                    @Override
                    public boolean hasField(Object target) {
                        return field.getDeclaringClass().isAssignableFrom(target.getClass());
                    }
                };
            }
        }

        if (target.getSuperclass() != null)
            return getFieldByName(target.getSuperclass(), name);

        throw new IllegalArgumentException("Cannot find field with name " + name + " in class " + target.getName());
    }

    private static <T> FieldAccessor<T> getField(
            final Class<?> target,
            final String name,
            final Class<T> fieldType,
            int index
    ) {
        for (final Field field : target.getDeclaredFields()) {
            if ((name == null || field.getName().equals(name)) && fieldType.isAssignableFrom(field.getType()) && index-- <= 0) {
                field.setAccessible(true);

                return new FieldAccessor<T>() {

                    @Override
                    public Class<T> getFieldClass() {
                        return fieldType;
                    }

                    @Override
                    public Field getField() {
                        return field;
                    }

                    @Override
                    @SuppressWarnings("unchecked")
                    public T get(Object target) {
                        try {
                            return (T) field.get(target);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("Cannot access reflection.", e);
                        }
                    }

                    @Override
                    public void set(Object target, Object value) {
                        try {
                            field.set(target, value);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("Cannot access reflection.", e);
                        }
                    }

                    @Override
                    public boolean hasField(Object target) {
                        return field.getDeclaringClass().isAssignableFrom(target.getClass());
                    }
                };
            }
        }

        if (target.getSuperclass() != null)
            return getField(target.getSuperclass(), name, fieldType, index);

        throw new IllegalArgumentException("Cannot find field with type " + fieldType);
    }

    /**
     * Finds any {@link Class} of the provided paths
     *
     * @param paths all possible class paths
     * @return false if the {@link Class} was NOT found
     */
    public static boolean hasClass(final String... paths) {
        for (final String path : paths) {
            if (getClass(path) != null) return true;
        }
        return false;
    }

    /**
     * Finds any {@link Class} of the provided paths
     *
     * @param paths all possible class paths
     * @return false if the {@link Class} was NOT found
     */
    public static boolean findClass(final String... paths) {
        for (final String path : paths) {
            if (getClass(path) != null) return true;
        }
        return false;
    }

    /**
     * A nullable {@link Class#forName(String)} instead of throwing exceptions
     *
     * @return null if the {@link Class} was NOT found
     */
    public static Class<?> getClass(final String path) {
        try {
            return Class.forName(path);
        } catch (final Exception ignored) {
            return null;
        }
    }

    /**
     * A nullable {@link Class#getDeclaredConstructor(Class[])} instead of throwing exceptions
     *
     * @return null if the {@link Constructor} was NOT found
     */
    public static Constructor<?> getConstructor(final Class<?> clazz, final Class<?>... classes) {
        try {
            return clazz.getDeclaredConstructor(classes);
        } catch (final Exception ignored) {
            return null;
        }
    }

    /**
     * Retrieve a method accessor for a specific method by parameter types.
     *
     * @param target - the target type.
     * @param parameterTypes - the parameter types of the method.
     * @return The method accessor.
     */
    public static MethodAccessor getMethod(Class<?> target, Class<?>... parameterTypes) {
        return getMethod(target, parameterTypes, 0);
    }

    /**
     * Retrieve a method accessor for a specific method by parameter types.
     *
     * @param className - lookup name of the class, see {@link #getClass(String)}.
     * @param parameterTypes - the parameter types of the method.
     * @return The method accessor.
     */
    public static MethodAccessor getMethod(String className, Class<?>... parameterTypes) {
        return getMethod(getClass(className), parameterTypes, 0);
    }

    /**
     * Retrieve a method accessor for a specific method by parameter types.
     *
     * @param target - the target type.
     * @param parameterTypes - the parameter types of the method.
     * @param index - the number of compatible methods to skip.
     * @return The method accessor.
     */
    public static MethodAccessor getMethod(Class<?> target, Class<?>[] parameterTypes, int index) {
        return getMethodByParameterTypes(target, parameterTypes, index);
    }

    public static MethodAccessor getMethod(Class<?> target, String methodName, Class<?>... parameterTypes) {
        for (final Method method : target.getDeclaredMethods()) {
            if (method.getName().equals(methodName) && isMethodCompatible(method, parameterTypes)) {
                method.setAccessible(true);

                return new MethodAccessor() {

                    @Override
                    public Object invoke(Object target, Object... args) {
                        try {
                            return method.invoke(target, args);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException("Cannot access reflection.", e);
                        }
                    }

                    @Override
                    public boolean hasMethod(Object target) {
                        return method.getDeclaringClass().isAssignableFrom(target.getClass());
                    }
                };
            }
        }

        if (target.getSuperclass() != null)
            return getMethod(target.getSuperclass(), methodName, parameterTypes);

        throw new IllegalArgumentException("Cannot find method " + methodName + " with given parameter types in class " + target.getName());
    }

    /**
     * Retrieve a method accessor for a specific method by parameter types.
     *
     * @param className - lookup name of the class, see {@link #getClass(String)}.
     * @param parameterTypes - the parameter types of the method.
     * @param index - the number of compatible methods to skip.
     * @return The method accessor.
     */
    public static MethodAccessor getMethod(String className, Class<?>[] parameterTypes, int index) {
        return getMethod(getClass(className), parameterTypes, index);
    }

    private static MethodAccessor getMethodByParameterTypes(
            final Class<?> target,
            final Class<?>[] parameterTypes,
            int index
    ) {
        for (final Method method : target.getDeclaredMethods()) {
            if (isMethodCompatible(method, parameterTypes) && index-- <= 0) {
                method.setAccessible(true);

                return new MethodAccessor() {

                    @Override
                    public Object invoke(Object target, Object... args) {
                        try {
                            return method.invoke(target, args);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException("Cannot access reflection.", e);
                        }
                    }

                    @Override
                    public boolean hasMethod(Object target) {
                        return method.getDeclaringClass().isAssignableFrom(target.getClass());
                    }
                };
            }
        }

        if (target.getSuperclass() != null)
            return getMethodByParameterTypes(target.getSuperclass(), parameterTypes, index);

        return null;
    }

    /**
     * Helper method to check if a method is compatible with the given parameter types.
     *
     * @param method - the method to check.
     * @param parameterTypes - the parameter types to match.
     * @return true if the method is compatible, false otherwise.
     */
    private static boolean isMethodCompatible(Method method, Class<?>[] parameterTypes) {
        Class<?>[] methodParams = method.getParameterTypes();

        if (methodParams.length != parameterTypes.length) {
            return false;
        }

        for (int i = 0; i < methodParams.length; i++) {
            if (parameterTypes[i] != null && !methodParams[i].isAssignableFrom(parameterTypes[i])) {
                return false;
            }
        }

        return true;
    }

    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> allFields = new ArrayList<>();
        Class<?> currentClass = clazz;

        while (currentClass != null && currentClass != Object.class) {
            allFields.addAll(Arrays.asList(currentClass.getDeclaredFields()));
            currentClass = currentClass.getSuperclass();
        }

        return allFields;
    }

}