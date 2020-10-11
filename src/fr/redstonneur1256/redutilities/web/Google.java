package fr.redstonneur1256.redutilities.web;

import fr.redstonneur1256.redutilities.io.Http;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.Validate;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLDecoder;
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

    public static List<Result> search(String keywords) {
        Validate.notEmpty(apiKey, "Api key cannot be null or empty");
        Validate.notEmpty(userAgent, "User Agent cannot be null or empty");

        List<Result> list = new ArrayList<>();

        try {
            String search = URLEncoder.encode(keywords, "UTF-8");
            String searchUrl = String.format(GOOGLE_URL, "018291224751151548851%3Ajzifriqvl1o", apiKey, 10, search);

            String json = Http.url(searchUrl).property("User-Agent", userAgent).read();

            JSONObject object = new JSONObject(json);
            if(!object.has("items")) {
                return list;
            }
            JSONArray items = object.getJSONArray("items");

            for(int i = 0; i < items.length(); i++) {
                JSONObject jsonResult = items.getJSONObject(i);
                Result result = new Result(
                        cleanString(jsonResult.getString("title")),
                        cleanString(jsonResult.getString("snippet")),
                        URLDecoder.decode(cleanString(jsonResult.getString("link")), "UTF-8")
                );
                list.add(result);
            }

        }catch(Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    private static String cleanString(String uncleanString) {
        return StringEscapeUtils.unescapeJava(
                StringEscapeUtils.unescapeHtml4(
                        uncleanString
                                .replaceAll("\\s+", " ")
                                .replaceAll("<.*?>", "")
                                .replaceAll("\"", "")));
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
