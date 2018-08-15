package kimstephenbovim.wordfeudtiles;

import kimstephenbovim.wordfeudtiles.pojo.LoginContent;

public class User {

    private String username;
    private String email;
    private String password;
    private long id;
    private String avatarRoot;
    private String loginMethod;
    private String fb_first_name;
    private String fb_middle_name;
    private String fb_last_name;

    public User(final LoginContent loginContent, final String loginMethod, final String password) {
        username = loginContent.getUsername();
        email = loginContent.getEmail();
        id = loginContent.getId();
        avatarRoot = loginContent.getAvatarRoot();
        fb_first_name = loginContent.getFbFirstName();
        fb_middle_name = loginContent.getFbMiddleName();
        fb_last_name = loginContent.getFbLastName();
        this.loginMethod = loginMethod;
        this.password = password;
    }

}
