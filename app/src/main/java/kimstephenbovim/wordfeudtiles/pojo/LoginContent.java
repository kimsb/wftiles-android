package kimstephenbovim.wordfeudtiles.pojo;

import com.squareup.moshi.Json;

public class LoginContent {

    @Json(name = "username")
    private String username;
    @Json(name = "id")
    private long id;
    @Json(name = "fb_first_name")
    private String fbFirstName;
    @Json(name = "fb_last_name")
    private String fbLastName;
    @Json(name = "fb_middle_name")
    private String fbMiddleName;
    @Json(name = "avatar_root")
    private String avatarRoot;
    @Json(name = "email")
    private String email;
    @Json(name = "type")
    private String type;

    public String getUsername() {
        return username;
    }

    public long getId() {
        return id;
    }

    public String getFbFirstName() {
        return fbFirstName;
    }

    public String getFbLastName() {
        return fbLastName;
    }

    public String getFbMiddleName() {
        return fbMiddleName;
    }

    public String getAvatarRoot() {
        return avatarRoot;
    }

    public String getEmail() {
        return email;
    }
}
