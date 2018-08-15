package kimstephenbovim.wordfeudtiles.pojo;

import com.squareup.moshi.Json;

public class LoginResponse {

    @Json(name = "status")
    private String status;
    @Json(name = "content")
    private LoginContent loginContent;

    public String getStatus() {
        return status;
    }

    public LoginContent getLoginContent() {
        return loginContent;
    }
}
