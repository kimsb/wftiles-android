package kimstephenbovim.wordfeudtiles.rest;

import com.squareup.moshi.Json;

public class MoveDTO {

    @Json(name = "points")
    private Integer points;
    @Json(name = "move_type")
    private String moveType;
    @Json(name = "user_id")
    private long userId;
    @Json(name = "main_word")
    private String mainWord;
    @Json(name = "tile_count")
    private Integer tileCount;

    public Integer getPoints() {
        return points;
    }

    public String getMoveType() {
        return moveType;
    }

    public long getUserId() {
        return userId;
    }

    public String getMainWord() {
        return mainWord;
    }

    public Integer getTileCount() {
        return tileCount;
    }
}
