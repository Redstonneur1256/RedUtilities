package fr.redstonneur1256.redutilities.reflection;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Modifier;

@SuppressWarnings("unchecked")
public abstract class ReflectiveAdapter<T> {

    private AccessibleObject object;

    public ReflectiveAdapter(AccessibleObject object) {
        this.object = object;
    }

    public boolean isPublic() {
        return hasModifier(Modifier.PUBLIC);
    }

    public boolean isPrivate() {
        return hasModifier(Modifier.PRIVATE);
    }

    public boolean isProtected() {
        return hasModifier(Modifier.PROTECTED);
    }

    public boolean isStatic() {
        return hasModifier(Modifier.STATIC);
    }

    public boolean isFinal() {
        return hasModifier(Modifier.FINAL);
    }

    public boolean isAccessible() {
        return object.isAccessible();
    }

    public T setAccessible(boolean accessible) {
        object.setAccessible(accessible);
        return (T) this;
    }

    protected boolean hasModifier(int modifier) {
        return (getModifiers() & modifier) == modifier;
    }

    public abstract int getModifiers();

}
