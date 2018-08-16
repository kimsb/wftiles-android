package kimstephenbovim.wordfeudtiles.rest;

import com.squareup.moshi.Json;

public class LoginWithEmailBody {

    @Json(name = "email")
    private String email;

    @Json(name = "password")
    private String password;

    public LoginWithEmailBody(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

}
