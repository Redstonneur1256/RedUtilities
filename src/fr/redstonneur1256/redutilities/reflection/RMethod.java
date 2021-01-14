package fr.redstonneur1256.redutilities.reflection;

import fr.redstonneur1256.redutilities.function.Functions;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class RMethod extends ReflectiveAdapter<RMethod> {

    private Method method;

    public RMethod(Method method) {
        super(method);

        this.method = method;
    }

    public boolean isSynchronized() {
        return hasModifier(Modifier.SYNCHRONIZED);
    }

    public boolean isNative() {
        return hasModifier(Modifier.NATIVE);
    }

    public boolean isAbstract() {
        return hasModifier(Modifier.ABSTRACT);
    }

    public Class<?> getReturnType() {
        return method.getReturnType();
    }

    public Object invoke(Object instance, Object... parameters) {
        return Functions.runtime(() -> method.invoke(instance, parameters));
    }

    @Override
    public int getModifiers() {
        return method.getModifiers();
    }

    public Method getMethod() {
        return method;
    }

}
