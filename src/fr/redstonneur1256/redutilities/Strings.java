package fr.redstonneur1256.redutilities;

public class Strings {

    public static String replaceLast(final String text, final String regex, final String replacement) {
        return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
    }

    public static int levenshtein(String a, String b) {
        int[][] map = new int[a.length()][b.length()];

        for(int x = 0; x < a.length(); x++) {
            map[x][0] = x;
        }
        for(int y = 0; y < b.length(); y++) {
            map[0][y] = y;
        }

        for(int x = 1; x < a.length(); x++) {
            for(int y = 1; y < b.length(); y++) {

                int cost = a.charAt(x) == b.charAt(y) ? 0 : 1;

                int va = map[x - 1][y] + 1;
                int vb = map[x][y - 1] + 1;
                int vc = map[x - 1][y - 1] + cost;

                int min = Maths.min(va, vb, vc);

                map[x][y] = min;
            }
        }

        return map[a.length() - 1][b.length() - 1] + 1;
    }

}
