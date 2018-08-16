package kimstephenbovim.wordfeudtiles.rest;

import com.squareup.moshi.Json;

public class LoginWithUsernameBody {

    @Json(name = "username")
    private String username;

    @Json(name = "password")
    private String password;

    public LoginWithUsernameBody(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

}
