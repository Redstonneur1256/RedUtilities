package fr.redstonneur1256.redutilities.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

public class RConstructor<T> extends ReflectiveAdapter<RConstructor<T>> {

    private Constructor<T> constructor;

    public RConstructor(Constructor<T> constructor) {
        super(constructor);

        this.constructor = constructor;
    }

    public Parameter[] getParameters() {
        return constructor.getParameters();
    }

    public Class<?>[] getParameterTypes() {
        return constructor.getParameterTypes();
    }

    /**
     * @param parameters the constructor parameters
     * @return the instance of the class or null if the build failed
     */
    public T build(Object... parameters) {
        try {
            return constructor.newInstance(parameters);
        }catch(Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public int getModifiers() {
        return constructor.getModifiers();
    }

    public Constructor<T> getConstructor() {
        return constructor;
    }

}
