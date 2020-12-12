package fr.redstonneur1256.redutilities.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RMethod {
    private final Method method;

    public RMethod(Method method) {
        this.method = method;
    }

    public boolean isAccessible() {
        return method.isAccessible();
    }

    public RMethod setAccessible(boolean accessible) {
        method.setAccessible(accessible);
        return this;
    }

    public Object invoke(Object instance, Object... parameters) {
        try {
            return method.invoke(instance, parameters);
        }catch(IllegalAccessException | InvocationTargetException exception) {
            throw new RuntimeException(exception);
        }
    }

}
