package fr.redstonneur1256.redutilities.reflection;

import java.util.Objects;

public class Reflection {

    public static RField getField(Class<?> clazz, String name) {
        Objects.requireNonNull(clazz, "Class cannot be null");
        Objects.requireNonNull(name, "Name cannot be null");
        try {
            return new RField(Reflect.getField(clazz, name));
        }catch(Exception exception) {
            throw error("field", name, clazz);
        }
    }

    public static RMethod getMethod(Class<?> clazz, String name, Class<?>... parameters) {
        Objects.requireNonNull(clazz, "Class cannot be null");
        Objects.requireNonNull(name, "Name cannot be null");
        try {
            return new RMethod(Reflect.getMethod(clazz, name, parameters));
        }catch(Exception exception) {
            throw error("method", name, clazz);
        }
    }

    public static <T> RConstructor<T> getConstructor(Class<T> clazz, Class<?>... parameters) {
        Objects.requireNonNull(clazz, "Class cannot be null");
        try {
            return new RConstructor<>(Reflect.getConstructor(clazz, parameters));
        }catch(Exception exception) {
            throw error("constructor", null, clazz);
        }
    }

    public static Class<?> getClass(String name) {
        try {
            return Class.forName(name);
        }catch(Exception exception) {
            throw new IllegalStateException("Class not found " + name);
        }
    }

    private static IllegalArgumentException error(String type, String name, Class<?> clazz) {
        String message = String.format("%s %s does not exist for class %s",
                type,
                name,
                clazz.getName());
        return new IllegalArgumentException(message);
    }

}