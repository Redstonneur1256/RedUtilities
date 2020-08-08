package fr.redstonneur1256.redutilities.io;

import java.util.HashMap;
import java.util.Map;

public class URLParser {

    private Map<String, String> data;
    public URLParser(String string) {
        data = new HashMap<>();
        if(string.contains("?")) {
            String message = string.split("\\?")[1];
            String[] parts = message.split("&");
            for(String s : parts) {
                int index = s.indexOf("=");
                if(index == -1) {
                    data.put(s, null);
                }else {
                    data.put(s.substring(0, index), s.substring(index+1));
                }
            }
        }
    }

    public boolean hasKey(String key) {
        return data.containsKey(key);
    }

    public String get(String key) {
        return data.get(key);
    }

}