package fr.redstonneur1256.redutilities.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class URLParser {

    /**
     * Parse an url arguments to a map, example:
     * <p>
     * <pre>http://example.com/?bar=foo&thing</pre>
     * Results in :
     * <pre>
     * bar = foo
     * thing = null
     * </pre>
     *
     * @param url the url to parse
     * @return the map of parsed arguments or {@code
     * Collections#emptyMap
     * }
     */
    public static Map<String, String> parseURL(String url) {
        int index = url.indexOf('?');
        if(index == -1) {
            return Collections.emptyMap();
        }
        url = url.substring(index + 1);
        Map<String, String> values = new HashMap<>();
        for(String part : url.split("&")) {
            int splitIndex = part.indexOf('=');
            String key = splitIndex == -1 ? part : part.substring(0, splitIndex);
            String value = splitIndex == -1 ? null : part.substring(splitIndex + 1);
            values.put(key, value);
        }
        return values;
    }

    public static String buildURL(Map<String, Object> parameters) throws UnsupportedEncodingException {
        if(parameters.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        for(Map.Entry<String, Object> entry : parameters.entrySet()) {
            if(builder.length() != 0) {
                builder.append('&');
            }

            builder.append(URLEncoder.encode(entry.getKey(), "UTF-8"))
                    .append('=')
                    .append(URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8"));
        }

        builder.insert(0, '?');
        return builder.toString();
    }

}