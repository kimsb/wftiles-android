package kimstephenbovim.wordfeudtiles.rest;

import com.squareup.moshi.Json;

public class GameContent {

    @Json(name = "game")
    public GameDTO gameDTO;
    @Json(name = "type")
    private String type;
}
