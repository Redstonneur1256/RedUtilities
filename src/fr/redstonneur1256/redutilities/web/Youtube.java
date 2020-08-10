package fr.redstonneur1256.redutilities.web;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Youtube {

    private static final Pattern VIDEO_ID_EXTRACTOR;
    static {
        VIDEO_ID_EXTRACTOR = Pattern.compile("(?<=watch\\?v=|/videos/|embed/|youtu.be/|/v/|/e/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#&?\\n]*");
    }

    public static String extractVideoId(String url) {
        Matcher matcher = VIDEO_ID_EXTRACTOR.matcher(url);
        if(matcher.find())
            return matcher.group();
        return null;
    }

}