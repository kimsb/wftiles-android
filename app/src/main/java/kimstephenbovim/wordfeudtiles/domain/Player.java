package kimstephenbovim.wordfeudtiles.domain;

import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kimstephenbovim.wordfeudtiles.Constants;
import kimstephenbovim.wordfeudtiles.WFTiles;

public class Player implements Serializable {
    private String username;
    private long id;
    private int score;
    private long avatarUpdated;
    private String fbFirstName;
    private String fbMiddleName;
    private String fbLastName;
    private List<String> rack;
    private int ruleset;

    public Player(String username, long id, int score, long avatarUpdated, String fbFirstName,
                  String fbMiddleName, String fbLastName, List<String> rack, int ruleset) {
        this.username = username;
        this.id = id;
        this.score = score;
        this.avatarUpdated = avatarUpdated;
        this.fbFirstName = fbFirstName;
        this.fbMiddleName = fbMiddleName;
        this.fbLastName = fbLastName;
        this.rack = rack;
        this.ruleset = ruleset;
    }

    public List<String> getRack() {
        if (rack == null || WFTiles.instance.getPreferences().isSortVowelsConsonants()) {
            return rack;
        }
        Collator collator = Collator.getInstance(Constants.shared.getLocale(ruleset));
        ArrayList<String> rackAlphabetical = new ArrayList<>(rack);
        Collections.sort(rackAlphabetical, collator);
        return rackAlphabetical;
    }

    public void setRack(final List<String> rack) {
        this.rack = rack;
    }

    public int getScore() {
        return score;
    }

    public String presentableUsername() {
        if (username.startsWith("_fb_") && fbFirstName != null && !"".equals(fbFirstName)) {
            return fbFirstName;
        }
        return username;
    }

    public long getId() {
        return id;
    }
}
