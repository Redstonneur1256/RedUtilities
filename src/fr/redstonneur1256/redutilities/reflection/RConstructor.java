package fr.redstonneur1256.redutilities.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@SuppressWarnings("unchecked")
public class RConstructor {
    private final Constructor<?> constructor;

    public RConstructor(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    public boolean isAccessible() {
        return constructor.isAccessible();
    }

    public RConstructor setAccessible(boolean b) {
        constructor.setAccessible(b);
        return this;
    }

    /**
     * @param parameters the constructor parameters
     * @return the instance of the class or null if the build failed
     */
    public <T> T build(Object... parameters) {
        try {
            return (T) constructor.newInstance(parameters);
        }catch(InstantiationException | IllegalAccessException | InvocationTargetException exception) {
            throw new RuntimeException(exception);
        }
    }
}
