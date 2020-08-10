package fr.redstonneur1256.redutilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Reader;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class Utils {

    private static final Gson GSON;
    private static final Pattern URL_PATTERN;

    static {
        GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        URL_PATTERN = Pattern.compile("^(?i)https?://[a-z0-9]*.[a-z]*.*$");
    }


    public static String toJson(Object object) {
        return GSON.toJson(object);
    }
    public static <T> T fromJson(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }
    public static <T> T fromJson(Reader reader, Class<T> clazz) {
        return GSON.fromJson(reader, clazz);
    }

    public static void sleep(TimeUnit unit, long time) {
        sleep(unit.toMillis(time));
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidURL(String url) {
        return URL_PATTERN.matcher(url).matches();
    }

    public static String sizeFormat(long size) {
        return sizeFormat(size, "o");
    }
    public static String sizeFormat(long size, String unit) {
        String[] units = new String[] {"", "K", "M", "G", "T", "P", "E", "Z, Y"};
        double tmpSize = size;
        int index = 0;
        while (tmpSize >= 1024 && index < units.length - 1) {
            tmpSize /= 1024.0;
            index++;
        }
        return String.format("%.2f %s%s", tmpSize, units[index], unit);
    }

}
