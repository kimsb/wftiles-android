package kimstephenbovim.wordfeudtiles.pojo;

import com.squareup.moshi.Json;

import java.util.List;

public class Player {

    @Json(name = "username")
    private String username;
    @Json(name = "avatar_updated")
    public Integer avatarUpdated;
    @Json(name = "is_local")
    public Boolean isLocal;
    @Json(name = "fb_last_name")
    public String fbLastName;
    @Json(name = "score")
    public Integer score;
    @Json(name = "fb_middle_name")
    public String fbMiddleName;
    @Json(name = "position")
    public Integer position;
    @Json(name = "id")
    public Integer id;
    @Json(name = "fb_first_name")
    public String fbFirstName;
    @Json(name = "rack")
    public List<String> rack = null;

    public String getUsername() {
        return username;
    }
}
