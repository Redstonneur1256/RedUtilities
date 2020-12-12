package fr.redstonneur1256.redutilities.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class RField {

    private static final RField modifiersField = Reflection.getField(Field.class, "modifiers", true).setAccessible(true);

    private final Field field;

    RField(Field field) {
        this.field = field;
    }

    public boolean isAccessible() {
        return field.isAccessible();
    }

    public RField setAccessible(boolean accessible) {
        field.setAccessible(accessible);
        return this;
    }

    public boolean isFinal() {
        return (field.getModifiers() & Modifier.FINAL) != 0;
    }

    public RField setFinal(boolean b) {
        if(b) {
            modifiersField.set(field, field.getModifiers() & Modifier.FINAL);
        }else {
            modifiersField.set(field, field.getModifiers() & ~Modifier.FINAL);
        }
        return this;
    }

    public void set(Object value) {
        set(null, value);
    }

    public void set(Object instance, Object value) {
        try {
            field.set(instance, value);
        }catch(IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    public Object get() {
        return get(null);
    }

    public Object get(Object instance) {
        try {
            return field.get(instance);
        }catch(IllegalAccessException e) {
            return null;
        }
    }

}