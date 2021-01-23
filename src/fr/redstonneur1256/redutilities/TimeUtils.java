package fr.redstonneur1256.redutilities;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class TimeUtils {

    public static final TimeUtils english;
    private static final Pattern amountPattern;
    private static final Map<Character, Long> units;

    static {
        english = new TimeUtils("and",
                new String[] { "year(s)", "month(s)", "day(s)", "hour(s)", "minute(s)", "second(s)", "millis" },
                new String[] { "Y", "M", "D", "H", "m", "s", "ms" });
        amountPattern = Pattern.compile("(\\(s\\))");

        Map<Character, Long> tmpUnits = new HashMap<>();
        tmpUnits.put('s', 1000L);
        tmpUnits.put('m', 1000L * 60);
        tmpUnits.put('h', 1000L * 60 * 60);
        tmpUnits.put('d', 1000L * 60 * 60 * 24);
        units = Collections.unmodifiableMap(tmpUnits);
    }

    private String and;
    private String[] identifiers;
    private String[] shortIdentifiers;

    public TimeUtils(String and, String[] identifiers, String[] shortIdentifiers) {
        this.and = and;
        this.identifiers = identifiers;
        this.shortIdentifiers = shortIdentifiers;
    }

    public String format(long millis, int level, boolean simple) {
        long years = millis / 31104000000L;
        long months = millis / 2592000000L % 12;
        long days = millis / 86400000L % 30;
        long hours = millis / 3600000L % 24;
        long minutes = millis / 60000L % 60;
        long seconds = millis / 1000L % 60;
        long rMillis = millis % 1000L;
        long[] values = new long[] { years, months, days, hours, minutes, seconds, rMillis };

        StringBuilder time = new StringBuilder();
        for(int i = 0, identifiersLength = identifiers.length - level; i < identifiersLength; i++) {
            long value = values[i];
            if(value == 0)
                continue;
            String text = simple ?
                    shortIdentifiers[i] :
                    format(identifiers[i], value != 1);
            time.append(value);
            if(!simple) {
                time.append(" ");
            }
            time.append(text).append(", ");
        }
        String finalTime = time.toString();
        finalTime = Strings.replaceLast(finalTime, ", ", "");
        finalTime = Strings.replaceLast(finalTime, ", ", simple ? " " : " " + and + " ");
        return finalTime;
    }

    private static String format(String text, boolean s) {
        return amountPattern.matcher(text).replaceAll(s ? "s" : "");
    }

    public String formatMaxYears(long millis, boolean simple) {
        return format(millis, 6, simple);
    }

    public String formatMaxMonths(long millis, boolean simple) {
        return format(millis, 5, simple);
    }

    public String formatMaxDays(long millis, boolean simple) {
        return format(millis, 4, simple);
    }

    public String formatMaxHours(long millis, boolean simple) {
        return format(millis, 3, simple);
    }

    public String formatMaxMinutes(long millis, boolean simple) {
        return format(millis, 2, simple);
    }

    public String formatMaxSeconds(long millis, boolean simple) {
        return format(millis, 1, simple);
    }

    public String formatMaxMillis(long millis, boolean simple) {
        return format(millis, 0, simple);
    }

    public static long parseTime(String input) {
        long time = 0;
        int position = 0;

        input = input.replace(" ", "");

        String[] parts = input.split("[smhd]");
        for(String string : parts) {
            try {
                position += string.length();
                char c = input.charAt(position++);
                long value = Long.parseLong(string);
                long unit = units.get(c);

                time += unit * value;
            }catch(Exception exception) {
                throw new IllegalArgumentException("Failed to parse time for input \"" + input + "\"", exception);
            }
        }

        return time;
    }

}
