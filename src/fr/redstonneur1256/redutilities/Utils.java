package fr.redstonneur1256.redutilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.regex.Pattern;

public class Utils {

    private static final Gson gson;
    private static final Pattern urlPattern;
    private static final String[] units;
    private static final char[] hexCodes;

    static {
        gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        urlPattern = Pattern.compile("^(?i)https?://[a-z0-9]*.[a-z]*.*$");
        units = new String[] {"", "K", "M", "G", "T", "P", "E", "Z, Y"};
        hexCodes = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
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
        return output.toString();
    }

    public static String binaryToHex(byte[] data) {
        StringBuilder builder = new StringBuilder(data.length * 2);
        for(byte b : data) {
            builder.append(hexCodes[(b >> 4) & 0xF]);
            builder.append(hexCodes[b & 0xF]);
        }
        return builder.toString();
    }

    public static byte[] hexToBinary(String hex) {
        String hexCode = new String(hexCodes);
        byte[] data = new byte[hex.length() / 2];
        for(int i = 0; i < hex.length(); i += 2) {
            int pos1 = hexCode.indexOf(hex.charAt(i));
            int pos2 = hexCode.indexOf(hex.charAt(i + 1));
            data[i / 2] = (byte) ((pos1 << 4) | pos2);
        }
        return data;
    }

}