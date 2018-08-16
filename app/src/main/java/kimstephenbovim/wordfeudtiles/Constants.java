package kimstephenbovim.wordfeudtiles;

import java.text.Collator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Constants {

    public static Constants shared = new Constants();
    private Map[] counts, points;
    private String[][] letters;
    private String[] locales = {"en", "nb", "nl", "da", "sv", "en", "es", "fr", "sv", "de", "nb", "fi", "pt"};

    public Map getCounts(int ruleset) {
        if (Texts.shared.unsupportedLanguage(ruleset)) {
            return new HashMap<>();
        }
        return counts[ruleset];
    }

    public Map getPoints(int ruleset) {
        if (Texts.shared.unsupportedLanguage(ruleset)) {
            return new HashMap<>();
        }
        return points[ruleset];
    }

    public String[] getLetters(int ruleset) {
        if (Texts.shared.unsupportedLanguage(ruleset)) {
            //TODO hva bør returneres her?
            //må ikke knekke om nytt språk innføres
            return new String[0];
        }
        //TODO
        /*if AppData.shared.sortByVowels() {
            return letters[ruleset]
        }*/

        String[] letterArray = Arrays.copyOf(letters[ruleset], letters[ruleset].length);
        Collator coll = Collator.getInstance(new Locale(locales[ruleset]));
        coll.setStrength(Collator.PRIMARY);
        Arrays.sort(letterArray, coll);
        return letterArray;
    }

    private Constants() {
        counts = new Map[]{arraysToMap(englishLetters, englishCounts),
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
        points = new Map[]{arraysToMap(englishLetters, englishPoints),
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

    //english
    private String[] englishLetters = {"", "A", "E", "I", "O", "U", "Y", "B", "C", "D", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "V", "W", "X", "Z"};
    private int[] englishCounts = {2, 10, 12, 9, 7, 4, 2, 2, 2, 5, 2, 3, 3, 1, 1, 4, 2, 6, 2, 1, 6, 5, 7, 2, 2, 1, 1};
    private int[] englishPoints = {0, 1, 1, 1, 1, 2, 4, 4, 4, 2, 4, 3, 4, 10, 5, 1, 3, 1, 4, 10, 1, 1, 1, 4, 4, 8, 10};

    //norwegian
    private String[] norwegianLetters = {"", "A", "E", "I", "O", "U", "Y", "Æ", "Ø", "Å", "B", "C", "D", "F", "G", "H", "J", "K", "L", "M", "N", "P", "R", "S", "T", "V", "W"};
    private int[] norwegianCounts = {2, 7, 9, 6, 4, 3, 1, 1, 2, 2, 3, 1, 5, 4, 4, 3, 2, 4, 5, 3, 6, 2, 7, 7, 7, 3, 1};
    private int[] norwegianPoints = {0, 1, 1, 2, 3, 4, 8, 8, 4, 4, 4, 10, 1, 2, 4, 3, 4, 3, 2, 2, 1, 4, 1, 1, 1, 5, 10};

    //dutch
    private String[] dutchLetters = {"", "A", "E", "I", "O", "U", "Y", "B", "C", "D", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "V", "W", "X", "Z"};
    private int[] dutchCounts = {2, 7, 18, 4, 6, 3, 1, 2, 2, 5, 2, 3, 2, 2, 3, 3, 3, 11, 2, 1, 5, 5, 5, 2, 2, 1, 2};
    private int[] dutchPoints = {0, 1, 1, 2, 1, 2, 8, 4, 5, 2, 4, 3, 4, 4, 3, 3, 3, 1, 4, 10, 2, 2, 2, 4, 5, 8, 5};

    //danish
    private String[] danishLetters = {"", "A", "E", "I", "O", "U", "Æ", "Ø", "Å", "B", "C", "D", "F", "G", "H", "J", "K", "L", "M", "N", "P", "R", "S", "T", "V", "X", "Y", "Z"};
    private int[] danishCounts = {2, 7, 9, 4, 5, 3, 2, 2, 2, 4, 2, 5, 3, 3, 2, 2, 4, 5, 3, 7, 2, 7, 6, 6, 3, 1, 2, 1};
    private int[] danishPoints = {0, 1, 1, 3, 2, 3, 4, 4, 4, 3, 8, 2, 3, 3, 4, 4, 3, 2, 4, 1, 4, 1, 2, 2, 4, 8, 4, 9};

    //swedish
    private String[] swedishLetters = {"", "A", "E", "I", "O", "U", "Y", "Å", "Ä", "Ö", "B", "C", "D", "F", "G", "H", "J", "K", "L", "M", "N", "P", "R", "S", "T", "V", "X", "Z"};
    private int[] swedishCounts = {2, 9, 8, 5, 6, 3, 1, 2, 2, 2, 2, 1, 5, 2, 3, 2, 1, 3, 5, 3, 6, 2, 8, 8, 9, 2, 1, 1};
    private int[] swedishPoints = {0, 1, 1, 1, 2, 4, 7, 4, 4, 4, 3, 8, 1, 3, 2, 3, 7, 3, 2, 3, 1, 4, 1, 1, 1, 3, 8, 8};

    //spanish
    private String[] spanishLetters = {"", "A", "E", "I", "O", "U", "B", "C", "CH", "D", "F", "G", "H", "J", "L", "LL", "M", "N", "Ñ", "P", "Q", "R", "RR", "S", "T", "V", "X", "Y", "Z"};
    private int[] spanishCounts = {2, 13, 13, 6, 9, 5, 2, 4, 1, 5, 1, 2, 2, 1, 4, 1, 2, 6, 1, 2, 1, 5, 1, 7, 4, 1, 1, 1, 1};
    private int[] spanishPoints = {0, 1, 1, 1, 1, 1, 3, 3, 5, 2, 4, 3, 4, 8, 1, 8, 3, 1, 8, 3, 5, 1, 8, 1, 2, 4, 8, 5, 10};

    //french
    private String[] frenchLetters = {"", "A", "E", "I", "O", "U", "Y", "B", "C", "D", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "V", "W", "X", "Z"};
    private int[] frenchCounts = {2, 10, 14, 9, 6, 6, 1, 2, 2, 3, 2, 3, 2, 1, 1, 5, 3, 6, 2, 1, 6, 6, 6, 2, 1, 1, 1};
    private int[] frenchPoints = {0, 1, 1, 1, 1, 1, 10, 3, 3, 2, 4, 2, 4, 8, 10, 2, 2, 1, 3, 8, 1, 1, 1, 5, 10, 10, 10};

    //german
    private String[] germanLetters = {"", "A", "Ä", "E", "I", "O", "Ö", "U", "Ü", "B", "C", "D", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "V", "W", "X", "Y", "Z"};
    private int[] germanCounts = {2, 5, 1, 14, 6, 3, 1, 6, 1, 2, 2, 5, 2, 3, 4, 1, 2, 3, 4, 9, 1, 1, 6, 7, 6, 1, 1, 1, 1, 1};
    private int[] germanPoints = {0, 1, 6, 1, 1, 2, 8, 1, 6, 3, 4, 1, 4, 2, 2, 6, 4, 2, 3, 1, 5, 10, 1, 1, 1, 6, 3, 8, 10, 3};

    //finnish
    private String[] finnishLetters = {"", "A", "E", "I", "O", "U", "Y", "Ä", "Ö", "B", "C", "D", "F", "G", "H", "J", "K", "L", "M", "N", "P", "R", "S", "T", "V"};
    private int[] finnishCounts = {2, 11, 9, 10, 5, 4, 2, 5, 1, 1, 1, 1, 1, 1, 2, 2, 6, 6, 3, 9, 2, 2, 7, 9, 2};
    private int[] finnishPoints = {0, 1, 1, 1, 2, 3, 4, 2, 7, 8, 10, 6, 8, 8, 4, 4, 3, 2, 3, 1, 4, 4, 1, 1, 4};

    //portugese
    private String[] portugeseLetters = {"", "A", "E", "I", "O", "U", "B", "C", "Ç", "D", "F", "G", "H", "J", "L", "M", "N", "P", "Q", "R", "S", "T", "V", "X", "Z"};
    private int[] portugeseCounts = {2, 12, 10, 9, 9, 6, 3, 3, 2, 4, 2, 2, 2, 2, 4, 5, 3, 3, 1, 5, 7, 4, 2, 1, 1};
    private int[] portugesePoints = {0, 1, 1, 1, 1, 2, 4, 2, 3, 2, 5, 4, 4, 6, 2, 1, 3, 2, 8, 1, 2, 2, 4, 10, 10};

    private Map<String, Integer> arraysToMap(String[] letters, int[] ints) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < letters.length; i++) {
            map.put(letters[i], ints[i]);
        }
        return map;
    }
}
