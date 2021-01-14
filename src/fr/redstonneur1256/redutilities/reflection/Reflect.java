package fr.redstonneur1256.redutilities.reflection;

import fr.redstonneur1256.redutilities.Utils;
import fr.redstonneur1256.redutilities.function.Provider;
import fr.redstonneur1256.redutilities.function.UnsafeProvider;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for simple fast reflection actions
 */
@SuppressWarnings("unchecked")
public class Reflect {

    private static final int javaVersion;

    static {
        int temp = -1;

        try {
            String rawVersion = System.getProperty("java.version");
            String[] parts = rawVersion.split("\\.");

            temp = Integer.parseInt(parts[0]);
            if(temp == 1) {
                temp = Integer.parseInt(parts[1]);
            }
        }catch(Exception exception) {
            Logger.getLogger(Utils.loggerName).log(Level.SEVERE, "Failed to get java version", exception);
        }

        javaVersion = temp;
    }

    public static int getJavaVersion() {
        return javaVersion;
    }

    public static <T> T get(Class<?> type, String fieldName) {
        return get(null, type, fieldName);
    }

    public static <T> T get(Object object, String fieldName) {
        return get(object, object.getClass(), fieldName);
    }

    public static <T> T get(Object instance, Class<?> type, String fieldName) {
        try {
            Field field = getField(type, fieldName);
            field.setAccessible(true);
            return (T) field.get(instance);
        }catch(Exception exception) {
            throw new RuntimeException(new NoSuchFieldException(fieldName));
        }
    }

    public static void set(Object instance, String field, Object value) {
        set(instance, instance.getClass(), field, value);
    }

    public static void set(Class<?> type, String field, Object value) {
        set(null, type, field, value);
    }

    public static void set(Object instance, Class<?> type, String fieldName, Object value) {
        try {
            Field field = getField(type, fieldName);
            field.setAccessible(true);
            field.set(instance, value);
        }catch(Exception exception) {
            throw new RuntimeException(new NoSuchFieldException(fieldName));
        }
    }

    public static Field getField(Class<?> type, String name) {
        return get(NoSuchFieldException::new, () -> type.getField(name), () -> type.getDeclaredField(name));
    }

    public static Method getMethod(Class<?> type, String name, Class<?>... parameters) {
        return get(NoSuchMethodException::new, () -> type.getMethod(name, parameters), () -> type.getDeclaredMethod(name, parameters));
    }

    public static <T> Constructor<T> getConstructor(Class<T> type, Class<?>... parameters) {
        return get(NoSuchMethodException::new, () -> type.getConstructor(parameters), () -> type.getDeclaredConstructor(parameters));
    }

    private static <T> T get(Provider<Exception> exceptionProvider, UnsafeProvider<T, ?>... providers) {
        for(UnsafeProvider<T, ?> provider : providers) {
            try {
                T value = provider.get();
                if(value != null) {
                    return value;
                }
            }catch(Throwable ignored) {
            }
        }
        throw new RuntimeException(exceptionProvider.get());
    }

}
