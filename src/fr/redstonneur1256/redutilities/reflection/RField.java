package fr.redstonneur1256.redutilities.reflection;

import fr.redstonneur1256.redutilities.function.Functions;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class RField extends ReflectiveAdapter<RField> {

    private static final RField modifiersField;

    static {
        if(Reflect.getJavaVersion() > 8) {
            modifiersField = null;
        }else {
            modifiersField = Reflection
                    .getField(Field.class, "modifiers")
                    .setAccessible(true);
        }
    }

    private Field field;

    public RField(Field field) {
        super(field);

        this.field = field;
    }


    public RField setFinal(boolean b) {
        if(modifiersField == null) {
            throw new IllegalStateException("Cannot remove final from a field with java > 8");
        }
        int modifiers = b ? field.getModifiers() | Modifier.FINAL : field.getModifiers() & ~Modifier.FINAL;
        modifiersField.set(field, modifiers);
        return this;
    }

    public boolean isVolatile() {
        return hasModifier(Modifier.VOLATILE);
    }


    public Class<?> getType() {
        return field.getType();
    }

    public void set(Object value) {
        set(null, value);
    }

    public void set(Object instance, Object value) {
        Functions.runtime(() -> field.set(instance, value));
    }

    public Object get() {
        return get(null);
    }

    public Object get(Object instance) {
        return Functions.runtime(() -> field.get(instance));
    }

    @Override
    public int getModifiers() {
        return field.getModifiers();
    }

    public Field getField() {
        return field;
    }

}