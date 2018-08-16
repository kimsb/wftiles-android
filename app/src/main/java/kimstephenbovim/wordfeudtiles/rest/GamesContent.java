package kimstephenbovim.wordfeudtiles.rest;

import com.squareup.moshi.Json;

import java.util.List;

public class GamesContent {

    @Json(name = "games")
    private List<GameDTO> games = null;
    @Json(name = "type")
    private String type;

    public List<GameDTO> getGames() {
        return games;
    }
}
