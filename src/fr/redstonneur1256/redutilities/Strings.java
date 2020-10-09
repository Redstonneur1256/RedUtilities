package fr.redstonneur1256.redutilities;

public class Strings {

    public static String replaceLast(final String text, final String regex, final String replacement) {
        return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
    }

}
