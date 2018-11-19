package kimstephenbovim.wordfeudtiles.domain;

import java.io.Serializable;

public class User implements Serializable{

    private String username;
    private String email;
    private String password;
    private long id;
    private String avatarRoot;
    private String loginMethod;
    private String fb_first_name;
    private String fb_middle_name;
    private String fb_last_name;

    public User(String username, String email, String password, long id, String avatarRoot, String loginMethod, String fb_first_name, String fb_middle_name, String fb_last_name) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.id = id;
        this.avatarRoot = avatarRoot;
        this.loginMethod = loginMethod;
        this.fb_first_name = fb_first_name;
        this.fb_middle_name = fb_middle_name;
        this.fb_last_name = fb_last_name;
    }

    public long getId() {
        return id;
    }

    public String presentableFullUsername() {
        if (username.startsWith("_fb_") && fb_first_name != null &&  !"".equals(fb_first_name)) {
            return fb_first_name +
                    (fb_middle_name == null ? "" : " " + fb_middle_name) +
                    (fb_last_name == null ? "" : " " + fb_last_name);
        }
        return username;
    }

    public String getAvatarRoot() {
        return avatarRoot;
    }

    public String getLoginMethod() {
        return loginMethod;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
