package kimstephenbovim.wordfeudtiles.rest;

import com.squareup.moshi.Json;

import java.util.List;

public class GameDTO {

    @Json(name = "updated")
    private long updated;
    @Json(name = "tiles")
    private List<List<Object>> tiles = null;
    @Json(name = "bag_count")
    private Integer bagCount;
    @Json(name = "is_running")
    private Boolean isRunning;
    @Json(name = "id")
    private long id;
    @Json(name = "current_player")
    private Integer currentPlayer;
    @Json(name = "created")
    private long created;
    @Json(name = "last_move")
    private MoveDTO lastMove;
    @Json(name = "players")
    private List<PlayerDTO> players = null;
    @Json(name = "ruleset")
    private Integer ruleset;

    public long getUpdated() {
        return updated;
    }

    public List<List<Object>> getTiles() {
        return tiles;
    }

    public Integer getBagCount() {
        return bagCount;
    }

    public Boolean getRunning() {
        return isRunning;
    }

    public long getId() {
        return id;
    }

    public Integer getCurrentPlayer() {
        return currentPlayer;
    }

    public long getCreated() {
        return created;
    }

    public MoveDTO getLastMove() {
        return lastMove;
    }

    public List<PlayerDTO> getPlayers() {
        return players;
    }

    public Integer getRuleset() {
        return ruleset;
    }
}
