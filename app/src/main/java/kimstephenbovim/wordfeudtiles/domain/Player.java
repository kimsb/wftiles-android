package kimstephenbovim.wordfeudtiles.domain;

import java.io.Serializable;
import java.util.List;

public class Player implements Serializable {
    private String username;
    private long id;
    private int score;
    private long avatarUpdated;
    private String fbFirstName;
    private String fbMiddleName;
    private String fbLastName;
    private List<String> rack;

    public Player(String username, long id, int score, long avatarUpdated,
                  String fbFirstName, String fbMiddleName, String fbLastName, List<String> rack) {
        this.username = username;
        this.id = id;
        this.score = score;
        this.avatarUpdated = avatarUpdated;
        this.fbFirstName = fbFirstName;
        this.fbMiddleName = fbMiddleName;
        this.fbLastName = fbLastName;
        this.rack = rack;
    }

    public List<String> getRack() {
        return rack;
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
