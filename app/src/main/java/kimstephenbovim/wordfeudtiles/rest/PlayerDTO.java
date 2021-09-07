package kimstephenbovim.wordfeudtiles.rest;

import com.squareup.moshi.Json;

import java.util.List;

public class PlayerDTO {

    @Json(name = "username")
    private String username;
    @Json(name = "avatar_updated")
    private double avatarUpdated;
    @Json(name = "is_local")
    private Boolean isLocal;
    @Json(name = "fb_last_name")
    private String fbLastName;
    @Json(name = "score")
    private Integer score;
    @Json(name = "fb_middle_name")
    private String fbMiddleName;
    @Json(name = "position")
    private Integer position;
    @Json(name = "id")
    private long id;
    @Json(name = "fb_first_name")
    private String fbFirstName;
    @Json(name = "rack")
    private List<String> rack = null;

    public String getUsername() {
        return username;
    }

    public double getAvatarUpdated() {
        return avatarUpdated;
    }

    public Boolean getLocal() {
        return isLocal;
    }

    public String getFbLastName() {
        return fbLastName;
    }

    public Integer getScore() {
        return score;
    }

    public String getFbMiddleName() {
        return fbMiddleName;
    }

    public Integer getPosition() {
        return position;
    }

    public long getId() {
        return id;
    }

    public String getFbFirstName() {
        return fbFirstName;
    }

    public List<String> getRack() {
        return rack;
    }
}
