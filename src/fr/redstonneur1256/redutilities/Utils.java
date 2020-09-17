package fr.redstonneur1256.redutilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class Utils {

    private static final Gson gson;
    private static final Pattern urlPattern;
    private static final  String[] units;

    static {
        gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        urlPattern = Pattern.compile("^(?i)https?://[a-z0-9]*.[a-z]*.*$");
        units = new String[] {"", "K", "M", "G", "T", "P", "E", "Z, Y"};
    }


    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public static <T> T fromJson(Reader reader, Class<T> clazz) {
        return gson.fromJson(reader, clazz);
    }

    public static void sleep(TimeUnit unit, long time) {
        sleep(unit.toMillis(time));
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidURL(String url) {
        return urlPattern.matcher(url).matches();
    }

    public static String sizeFormat(long size) {
        return sizeFormat(size, "o", 1000);
    }

    public static String sizeFormat(long size, String unit) {
        return sizeFormat(size, unit, 1000);
    }

    public static String sizeFormat(long size, String unit, int divisor) {
        double tmpSize = size;
        int index = 0;
        while(tmpSize >= divisor && index < units.length - 1) {
            tmpSize /= divisor;
            index++;
        }
        return String.format("%.2f %s%s", tmpSize, units[index], unit);
    }

    public static String errorMessage(Throwable throwable) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(output);
        throwable.printStackTrace(writer);
        writer.close();
        return new String(output.toByteArray());
    }

}
