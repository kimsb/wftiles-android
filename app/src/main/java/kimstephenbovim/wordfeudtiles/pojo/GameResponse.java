package kimstephenbovim.wordfeudtiles.pojo;

import com.squareup.moshi.Json;

public class GameResponse {

    @Json(name = "status")
    private String status;
    @Json(name = "content")
    private GameContent gameContent;

    public String getStatus() {
        return status;
    }

    public GameContent getGameContent() {
        return gameContent;
    }
}
