package kimstephenbovim.wordfeudtiles.pojo;

import com.squareup.moshi.Json;

public class GameContent {

    @Json(name = "game")
    public Game game;
    @Json(name = "type")
    private String type;
}
