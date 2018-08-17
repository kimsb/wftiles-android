package kimstephenbovim.wordfeudtiles.domain;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String username;
    private long id;
    private int score;
    private long avatarUpdated;
    private String fbFirstName;
    private String fbMiddleName;
    private String fbLastName;
    private List<String> rack = new ArrayList<>();

    public Player(String username, long id, int score, long avatarUpdated, String fbFirstName, String fbMiddleName, String fbLastName) {
        this.username = username;
        this.id = id;
        this.score = score;
        this.avatarUpdated = avatarUpdated;
        this.fbFirstName = fbFirstName;
        this.fbMiddleName = fbMiddleName;
        this.fbLastName = fbLastName;
    }

    public List<String> getRack() {
        return rack;
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
