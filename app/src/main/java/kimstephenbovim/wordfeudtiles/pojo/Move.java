package kimstephenbovim.wordfeudtiles.pojo;

import com.squareup.moshi.Json;

public class Move {

    @Json(name = "points")
    public Integer points;
    @Json(name = "move_type")
    public String moveType;
    @Json(name = "user_id")
    public Integer userId;
    @Json(name = "main_word")
    private String mainWord;

    public String getMainWord() {
        return mainWord;
    }
}
