package fr.redstonneur1256.redutilities.web;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.redstonneur1256.redutilities.Validate;
import fr.redstonneur1256.redutilities.io.Http;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Google {

    private static final String GOOGLE_URL = "https://www.googleapis.com/customsearch/v1?safe=medium&cx=%s&key=%s&num=%d&q=%s";
    private static String apiKey;
    private static String userAgent;

    public static void setup(String key, String userAgent) {
        Google.apiKey = key;
        Google.userAgent = userAgent;
    }

    public static List<Result> search(String keywords) throws IOException {
        Validate.notEmpty(apiKey, "Api key cannot be null or empty");
        Validate.notEmpty(userAgent, "User Agent cannot be null or empty");

        List<Result> list = new ArrayList<>();

        String search = URLEncoder.encode(keywords, "UTF-8");
        String searchUrl = String.format(GOOGLE_URL, "018291224751151548851%3Ajzifriqvl1o", apiKey, 10, search);

        String json = Http.url(searchUrl).property("User-Agent", userAgent).read();

        JsonObject object = JsonParser.parseString(json).getAsJsonObject();

        if(!object.has("items")) {
            return list;
        }

        JsonArray items = object.get("items").getAsJsonArray();

        for(int i = 0; i < items.size(); i++) {
            JsonObject item = items.get(i).getAsJsonObject();

            Result results = new Result(
                    item.get("title").getAsString(),
                    item.get("snippet").getAsString(),
                    item.get("link").getAsString()
            );
            list.add(results);
        }

        return list;
    }

    public static class Result {

        private String title;
        private String snippet;
        private String link;

        public Result(String title, String snippet, String link) {
            this.title = title;
            this.snippet = snippet;
            this.link = link;
        }

        public String getTitle() {
            return title;
        }

        public String getSnippet() {
            return snippet;
        }

        public String getLink() {
            return link;
        }
    }

}
