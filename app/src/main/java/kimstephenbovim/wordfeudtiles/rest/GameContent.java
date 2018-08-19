package kimstephenbovim.wordfeudtiles.rest;

import com.squareup.moshi.Json;

public class GameContent {

    @Json(name = "game")
    private GameDTO gameDTO;
    @Json(name = "type")
    private String type;

    public GameDTO getGameDTO() {
        return gameDTO;
    }
}
