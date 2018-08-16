package kimstephenbovim.wordfeudtiles.rest;

import com.squareup.moshi.Json;

public class GamesResponse {

    @Json(name = "status")
    private String status;
    @Json(name = "content")
    private GamesContent gamesContent;

    public String getStatus() {
        return status;
    }

    public GamesContent getGamesContent() {
        return gamesContent;
    }
}
