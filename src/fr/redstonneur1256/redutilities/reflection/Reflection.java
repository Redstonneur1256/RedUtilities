package fr.redstonneur1256.redutilities.reflection;

import java.util.Arrays;
import java.util.Objects;

public class Reflection {
    public static RField getField(Class<?> clazz, String name, boolean declared) {
        Objects.requireNonNull(clazz, "Class cannot be null");
        Objects.requireNonNull(name, "Name cannot be null");
        try {
            return new RField(declared ? clazz.getDeclaredField(name) : clazz.getField(name));
        }catch(NoSuchFieldException e) {
            throw error(declared, "field", name, clazz);
        }
    }

    public static RMethod getMethod(Class<?> clazz, String name, boolean declared, Class<?>... parameters) {
        Objects.requireNonNull(clazz, "Class cannot be null");
        Objects.requireNonNull(name, "Name cannot be null");
        try {
            return new RMethod(declared ? clazz.getDeclaredMethod(name, parameters) : clazz.getMethod(name, parameters));
        }catch(NoSuchMethodException e) {
            throw error(declared, "method", name, clazz);
        }
    }

    public static RConstructor getConstructor(Class<?> clazz, boolean declared, Class<?>... parameters) {
        Objects.requireNonNull(clazz, "Class cannot be null");
        try {
            return new RConstructor(declared ? clazz.getDeclaredConstructor(parameters) : clazz.getConstructor(parameters));
        }catch(Exception e) {
            throw new IllegalArgumentException((declared ? "declared" : "not declared") + " constructor (" +
                    Arrays.toString(parameters) + ") does not exist for class " + clazz.getName());
        }
    }

    public static RConstructor getFirstConstructor(Class<?> clazz, boolean declared) {
        try {
            return new RConstructor(declared ? clazz.getDeclaredConstructors()[0] : clazz.getConstructors()[0]);
        }catch(Exception ignored) {
        }
        throw new IllegalArgumentException((declared ? "declared" : "not declared") + " constructor does not exist for class " + clazz.getName());
    }

    public static Class<?> getClass(String name) {
        try {
            return Class.forName(name);
        }catch(Exception e) {
            throw new IllegalStateException("Class not found " + name);
        }
    }

    private static IllegalArgumentException error(boolean declared, String type, String name, Class<?> clazz) {
        String message = String.format("%s %s %s does not exist for class %s",
                (declared ? "declared" : "not declared"),
                type,
                name,
                clazz.getName());
        return new IllegalArgumentException(message);
    }

}