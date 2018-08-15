package kimstephenbovim.wordfeudtiles.pojo;

import com.squareup.moshi.Json;

import java.util.List;

public class Game {

    @Json(name = "updated")
    private Integer updated;
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
    private Integer created;
    @Json(name = "last_move")
    private Move lastMove;
    @Json(name = "players")
    private List<Player> players = null;
    @Json(name = "ruleset")
    private Integer ruleset;
    private Player opponent;

    public Integer getUpdated() {
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

    public Integer getCreated() {
        return created;
    }

    public Move getLastMove() {
        return lastMove;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Integer getRuleset() {
        return ruleset;
    }

    public Player getOpponent() {
        return opponent;
    }
}
