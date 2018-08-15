package kimstephenbovim.wordfeudtiles.pojo;

import com.squareup.moshi.Json;

import java.util.List;

public class GamesContent {

    @Json(name = "games")
    private List<Game> games = null;
    @Json(name = "type")
    private String type;

    public List<Game> getGames() {
        return games;
    }
}
