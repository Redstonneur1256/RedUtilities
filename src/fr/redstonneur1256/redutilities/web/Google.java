package fr.redstonneur1256.redutilities.web;

import fr.redstonneur1256.redutilities.io.Http;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.Validate;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Google {

    private static final String GOOGLE_URL = "https://www.googleapis.com/customsearch/v1?safe=medium&cx=%s&key=%s&num=%d&q=%s";
    private static String API_KEY;
    private static String USER_AGENT;

    public static void setup(String key, String userAgent) {
        API_KEY = key;
        USER_AGENT = userAgent;
    }

    public static List<Result> search(String keywords) {
        Validate.notEmpty(API_KEY, "Api key cannot be null or empty");
        Validate.notEmpty(USER_AGENT, "User Agent cannot be null or empty");

        List<Result> list = new ArrayList<>();

        try {
            String search = URLEncoder.encode(keywords, "UTF-8");
            String searchUrl = String.format(GOOGLE_URL, "018291224751151548851%3Ajzifriqvl1o", API_KEY, 10, search);

            String json = Http.url(searchUrl).property("User-Agent", USER_AGENT).read();

            JSONObject object = new JSONObject(json);
            if(!object.has("items")) {
                return list;
            }
            JSONArray items = object.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject jsonResult = items.getJSONObject(i);
                Result result = new Result(
                        cleanString(jsonResult.getString("title")),
                        cleanString(jsonResult.getString("snippet")),
                        URLDecoder.decode(cleanString(jsonResult.getString("link")), "UTF-8")
                );
                list.add(result);
            }

        } catch (Exception e) {
            e.printStackTrace();
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
        public String getTitle() { return title; }
        public String getSnippet() { return snippet; }
        public String getLink() { return link; }
    }


    private static String cleanString(String uncleanString) {
        return StringEscapeUtils.unescapeJava(
                StringEscapeUtils.unescapeHtml4(
                        uncleanString
                                .replaceAll("\\s+", " ")
                                .replaceAll("<.*?>", "")
                                .replaceAll("\"", "")));
    }

}
