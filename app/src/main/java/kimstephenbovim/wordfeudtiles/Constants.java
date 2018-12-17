package kimstephenbovim.wordfeudtiles;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Constants {

    public static Constants shared = new Constants();
    static String MESSAGE_SKIP_LOGIN = "MESSAGE_SKIP_LOGIN";
    static String MESSAGE_GAME_ID = "MESSAGE_GAME_ID";
    static String MESSAGE_OPPONENT_NAME = "MESSAGE_OPPONENT_NAME";
    static String MESSAGE_IS_TWOPANE = "MESSAGE_IS_TWOPANE";
    static String MESSAGE_SHOW_TWOPANE_GAME = "MESSAGE_SHOW_TWOPANE_GAME";

    private HashMap<Integer, List<TileParameters>> tileParameters = new HashMap<>();
    private HashMap<Integer, List<TileOverviewParameters>> tileOverviewParameters = new HashMap<>();
    private HashMap[] counts, points;
    private String[][] letters;
    private String[] locales = {"en", "nb", "nl", "da", "sv", "en", "es", "fr", "sv", "de", "nb", "fi", "pt"};

    public Locale getLocale(final int ruleset) {
        return new Locale(locales[ruleset]);
    }

    public String[] getLetters(int ruleset) {
        return letters[ruleset];
    }

    HashMap<String, Integer> getCounts(int ruleset) {
        if (Texts.shared.unsupportedLanguage(ruleset)) {
            return new HashMap<>();
        }
        return copyOfMap(counts[ruleset]);
    }

    HashMap<String, Integer> getPoints(int ruleset) {
        if (Texts.shared.unsupportedLanguage(ruleset)) {
            return new HashMap<>();
        }
        return copyOfMap(points[ruleset]);
    }

    private Constants() {
        counts = new HashMap[]{arraysToMap(englishLetters, englishCounts),
                arraysToMap(norwegianLetters, norwegianCounts),
                arraysToMap(dutchLetters, dutchCounts),
                arraysToMap(danishLetters, danishCounts),
                arraysToMap(swedishLetters, swedishCounts),
                arraysToMap(englishLetters, englishCounts),
                arraysToMap(spanishLetters, spanishCounts),
                arraysToMap(frenchLetters, frenchCounts),
                arraysToMap(swedishLetters, swedishCounts),
                arraysToMap(germanLetters, germanCounts),
                arraysToMap(norwegianLetters, norwegianCounts),
                arraysToMap(finnishLetters, finnishCounts),
                arraysToMap(portugeseLetters, portugeseCounts)};
        points = new HashMap[]{arraysToMap(englishLetters, englishPoints),
                arraysToMap(norwegianLetters, norwegianPoints),
                arraysToMap(dutchLetters, dutchPoints),
                arraysToMap(danishLetters, danishPoints),
                arraysToMap(swedishLetters, swedishPoints),
                arraysToMap(englishLetters, englishPoints),
                arraysToMap(spanishLetters, spanishPoints),
                arraysToMap(frenchLetters, frenchPoints),
                arraysToMap(swedishLetters, swedishPoints),
                arraysToMap(germanLetters, germanPoints),
                arraysToMap(norwegianLetters, norwegianPoints),
                arraysToMap(finnishLetters, finnishPoints),
                arraysToMap(portugeseLetters, portugesePoints)};
        letters = new String[][]{englishLetters, norwegianLetters, dutchLetters, danishLetters, swedishLetters, englishLetters,
                spanishLetters, frenchLetters, swedishLetters, germanLetters, norwegianLetters, finnishLetters, portugeseLetters};

    }

    int getAvailableWidth() {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        int minMargin = Math.round(WFTiles.instance.getResources().getDimension(R.dimen.min_margin));

        return metrics.widthPixels / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT) > 900
                ? Math.round(metrics.widthPixels
                - (minMargin * 4)
                - WFTiles.instance.getResources().getDimension(R.dimen.two_pane_list_width)
                - WFTiles.instance.getResources().getDimension(R.dimen.linear_layout_divider_width))
                : metrics.widthPixels - (minMargin * 2);
    }

    List<TileParameters> getTileParameters() {
        int availableWidth = getAvailableWidth();
        if (tileParameters.containsKey(availableWidth)) {
            return tileParameters.get(availableWidth);
        }

        ArrayList<TileParameters> parameters = new ArrayList<>();

        int tileSize = Math.round(WFTiles.instance.getResources().getDimension(R.dimen.tile_width));
        int minMargin = Math.round(WFTiles.instance.getResources().getDimension(R.dimen.min_margin));
        int minSpacing = Math.round(WFTiles.instance.getResources().getDimension(R.dimen.tile_grid_min_space));

        int span = (availableWidth + minSpacing) / (tileSize + minSpacing);

        int extraSpacing = availableWidth - (span * tileSize) - ((span - 1) * minSpacing);
        int spacing = minSpacing;
        while (extraSpacing > span - 1) {
            spacing++;
            extraSpacing -= span - 1;
        }

        int extraMargin = extraSpacing > 0
                ? Math.round(extraSpacing / 2f)
                : 0;

        //TODO kan det noen gang være mer enn 97 (104 - 7 på racket) brikker som skal vises?
        int column = 0;
        int row = 0;
        for (int i = 0; i < 97; i++) {
            int rowPlusTile = row * (tileSize + spacing);
            int columnPlusTile = extraMargin + column * (tileSize + spacing);
            parameters.add(new TileParameters(rowPlusTile + minMargin,
                    rowPlusTile + tileSize + minMargin,
                    columnPlusTile + tileSize,
                    columnPlusTile,
                    columnPlusTile + (tileSize / 2),
                    rowPlusTile + tileSize - (tileSize / 5) + minMargin,
                    Math.round(columnPlusTile + (tileSize * 0.9f)),
                    Math.round(rowPlusTile + (tileSize * 0.3f)) + minMargin));
            if (++column == span) {
                column = 0;
                row++;
            }
        }
        tileParameters.put(availableWidth, parameters);
        return parameters;
    }

    List<TileOverviewParameters> getTileOverviewParameters() {
        int availableWidth = getAvailableWidth();
        if (tileOverviewParameters.containsKey(availableWidth)) {
            return tileOverviewParameters.get(availableWidth);
        }

        ArrayList<TileOverviewParameters> parameters = new ArrayList<>();

        int tileSize = Math.round(WFTiles.instance.getResources().getDimension(R.dimen.tile_width));
        int tileOverviewSize = Math.round(WFTiles.instance.getResources().getDimension(R.dimen.tile_overview_width));
        int minMargin = Math.round(WFTiles.instance.getResources().getDimension(R.dimen.min_margin));
        int minSpacing = Math.round(WFTiles.instance.getResources().getDimension(R.dimen.tile_grid_min_space));

        int span = (availableWidth + minSpacing) / (tileOverviewSize + minSpacing);

        int extraSpacing = availableWidth - (span * tileOverviewSize) - ((span - 1) * minSpacing);
        int spacing = minSpacing;
        while (extraSpacing > span - 1) {
            spacing++;
            extraSpacing -= span - 1;
        }

        int extraMargin = extraSpacing > 0
                ? Math.round(extraSpacing / 2f)
                : 0;

        //TODO kan det noen gang være mer enn 30 (german) forskjellige brikker?
        int column = 0;
        int row = 0;
        for (int i = 0; i < 30; i++) {
            int rowPlusTile = row * (tileSize + spacing);
            int columnPlusTile = extraMargin + column * (tileOverviewSize + spacing);
            parameters.add(new TileOverviewParameters(rowPlusTile + minMargin,
                    rowPlusTile + tileSize + minMargin,
                    columnPlusTile + tileOverviewSize,
                    columnPlusTile,
                    columnPlusTile + tileSize,
                    columnPlusTile + (tileSize / 2),
                    rowPlusTile + tileSize - (tileSize / 5) + minMargin,
                    Math.round(columnPlusTile + (tileSize * 0.9f)),
                    Math.round(rowPlusTile + (tileSize * 0.3f)) + minMargin,
                    Math.round(columnPlusTile + tileSize + (tileSize * 0.1f)),
                    Math.round(rowPlusTile + tileSize - (tileSize * 0.3f) + minMargin)));
            if (++column == span) {
                column = 0;
                row++;
            }
        }
        tileOverviewParameters.put(availableWidth, parameters);
        return parameters;
    }

    //english
    private final String[] englishLetters = {"", "A", "E", "I", "O", "U", "Y", "B", "C", "D", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "V", "W", "X", "Z"};
    private final int[] englishCounts = {2, 10, 12, 9, 7, 4, 2, 2, 2, 5, 2, 3, 3, 1, 1, 4, 2, 6, 2, 1, 6, 5, 7, 2, 2, 1, 1};
    private final int[] englishPoints = {0, 1, 1, 1, 1, 2, 4, 4, 4, 2, 4, 3, 4, 10, 5, 1, 3, 1, 4, 10, 1, 1, 1, 4, 4, 8, 10};

    //norwegian
    private final String[] norwegianLetters = {"", "A", "E", "I", "O", "U", "Y", "Æ", "Ø", "Å", "B", "C", "D", "F", "G", "H", "J", "K", "L", "M", "N", "P", "R", "S", "T", "V", "W"};
    private final int[] norwegianCounts = {2, 7, 9, 6, 4, 3, 1, 1, 2, 2, 3, 1, 5, 4, 4, 3, 2, 4, 5, 3, 6, 2, 7, 7, 7, 3, 1};
    private final int[] norwegianPoints = {0, 1, 1, 2, 3, 4, 8, 8, 4, 4, 4, 10, 1, 2, 4, 3, 4, 3, 2, 2, 1, 4, 1, 1, 1, 5, 10};

    //dutch
    private final String[] dutchLetters = {"", "A", "E", "I", "O", "U", "Y", "B", "C", "D", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "V", "W", "X", "Z"};
    private final int[] dutchCounts = {2, 7, 18, 4, 6, 3, 1, 2, 2, 5, 2, 3, 2, 2, 3, 3, 3, 11, 2, 1, 5, 5, 5, 2, 2, 1, 2};
    private final int[] dutchPoints = {0, 1, 1, 2, 1, 2, 8, 4, 5, 2, 4, 3, 4, 4, 3, 3, 3, 1, 4, 10, 2, 2, 2, 4, 5, 8, 5};

    //danish
    private final String[] danishLetters = {"", "A", "E", "I", "O", "U", "Æ", "Ø", "Å", "B", "C", "D", "F", "G", "H", "J", "K", "L", "M", "N", "P", "R", "S", "T", "V", "X", "Y", "Z"};
    private final int[] danishCounts = {2, 7, 9, 4, 5, 3, 2, 2, 2, 4, 2, 5, 3, 3, 2, 2, 4, 5, 3, 7, 2, 7, 6, 6, 3, 1, 2, 1};
    private final int[] danishPoints = {0, 1, 1, 3, 2, 3, 4, 4, 4, 3, 8, 2, 3, 3, 4, 4, 3, 2, 4, 1, 4, 1, 2, 2, 4, 8, 4, 9};

    //swedish
    private final String[] swedishLetters = {"", "A", "E", "I", "O", "U", "Y", "Å", "Ä", "Ö", "B", "C", "D", "F", "G", "H", "J", "K", "L", "M", "N", "P", "R", "S", "T", "V", "X", "Z"};
    private final int[] swedishCounts = {2, 9, 8, 5, 6, 3, 1, 2, 2, 2, 2, 1, 5, 2, 3, 2, 1, 3, 5, 3, 6, 2, 8, 8, 9, 2, 1, 1};
    private final int[] swedishPoints = {0, 1, 1, 1, 2, 4, 7, 4, 4, 4, 3, 8, 1, 3, 2, 3, 7, 3, 2, 3, 1, 4, 1, 1, 1, 3, 8, 8};

    //spanish
    private final String[] spanishLetters = {"", "A", "E", "I", "O", "U", "B", "C", "CH", "D", "F", "G", "H", "J", "L", "LL", "M", "N", "Ñ", "P", "Q", "R", "RR", "S", "T", "V", "X", "Y", "Z"};
    private final int[] spanishCounts = {2, 13, 13, 6, 9, 5, 2, 4, 1, 5, 1, 2, 2, 1, 4, 1, 2, 6, 1, 2, 1, 5, 1, 7, 4, 1, 1, 1, 1};
    private final int[] spanishPoints = {0, 1, 1, 1, 1, 1, 3, 3, 5, 2, 4, 3, 4, 8, 1, 8, 3, 1, 8, 3, 5, 1, 8, 1, 2, 4, 8, 5, 10};

    //french
    private final String[] frenchLetters = {"", "A", "E", "I", "O", "U", "Y", "B", "C", "D", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "V", "W", "X", "Z"};
    private final int[] frenchCounts = {2, 10, 14, 9, 6, 6, 1, 2, 2, 3, 2, 3, 2, 1, 1, 5, 3, 6, 2, 1, 6, 6, 6, 2, 1, 1, 1};
    private final int[] frenchPoints = {0, 1, 1, 1, 1, 1, 10, 3, 3, 2, 4, 2, 4, 8, 10, 2, 2, 1, 3, 8, 1, 1, 1, 5, 10, 10, 10};

    //german
    private final String[] germanLetters = {"", "A", "Ä", "E", "I", "O", "Ö", "U", "Ü", "B", "C", "D", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "V", "W", "X", "Y", "Z"};
    private final int[] germanCounts = {2, 5, 1, 14, 6, 3, 1, 6, 1, 2, 2, 5, 2, 3, 4, 1, 2, 3, 4, 9, 1, 1, 6, 7, 6, 1, 1, 1, 1, 1};
    private final int[] germanPoints = {0, 1, 6, 1, 1, 2, 8, 1, 6, 3, 4, 1, 4, 2, 2, 6, 4, 2, 3, 1, 5, 10, 1, 1, 1, 6, 3, 8, 10, 3};

    //finnish
    private final String[] finnishLetters = {"", "A", "E", "I", "O", "U", "Y", "Ä", "Ö", "B", "C", "D", "F", "G", "H", "J", "K", "L", "M", "N", "P", "R", "S", "T", "V"};
    private final int[] finnishCounts = {2, 11, 9, 10, 5, 4, 2, 5, 1, 1, 1, 1, 1, 1, 2, 2, 6, 6, 3, 9, 2, 2, 7, 9, 2};
    private final int[] finnishPoints = {0, 1, 1, 1, 2, 3, 4, 2, 7, 8, 10, 6, 8, 8, 4, 4, 3, 2, 3, 1, 4, 4, 1, 1, 4};

    //portugese
    private final String[] portugeseLetters = {"", "A", "E", "I", "O", "U", "B", "C", "Ç", "D", "F", "G", "H", "J", "L", "M", "N", "P", "Q", "R", "S", "T", "V", "X", "Z"};
    private final int[] portugeseCounts = {2, 12, 10, 9, 9, 6, 3, 3, 2, 4, 2, 2, 2, 2, 4, 5, 3, 3, 1, 5, 7, 4, 2, 1, 1};
    private final int[] portugesePoints = {0, 1, 1, 1, 1, 2, 4, 2, 3, 2, 5, 4, 4, 6, 2, 1, 3, 2, 8, 1, 2, 2, 4, 10, 10};

    private HashMap<String, Integer> arraysToMap(String[] letters, int[] ints) {
        HashMap<String, Integer> map = new HashMap<>();
        for (int i = 0; i < letters.length; i++) {
            map.put(letters[i], ints[i]);
        }
        return map;
    }

    private HashMap<String, Integer> copyOfMap(HashMap<String, Integer> map) {
        HashMap<String, Integer> mapCopy = new HashMap<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            mapCopy.put(entry.getKey(), entry.getValue());
        }
        return mapCopy;
    }
}
