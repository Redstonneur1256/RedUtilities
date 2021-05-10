package fr.redstonneur1256.redutilities;

public class Validate {

    public static void nonNull(Object value, String name) {
        if(value == null) {
            throw new NullPointerException(name + " cannot be null");
        }
    }

    public static void isTrue(boolean value, String message) {
        if(!value) {
            throw new IllegalStateException(message);
        }
    }

    public static void notEmpty(String string, String message) {
        isTrue(string != null && !string.isEmpty(), message);
    }

}
