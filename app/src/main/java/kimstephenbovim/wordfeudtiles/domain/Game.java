package kimstephenbovim.wordfeudtiles.domain;

import java.util.Map;

public class Game {
    private long updated;
    private Map<String, Integer> letterCount;
    private boolean isRunning;
    private long id;
    private boolean playersTurn;
    private Move lastMove;
    private Player player;
    private Player opponent;
    private int ruleset;
    //private int bagCount;
    //private long created;

    public Game(long updated, Map<String, Integer> letterCount, boolean isRunning, long id, boolean playersTurn, Move lastMove, Player player, Player opponent, int ruleset) {
        this.updated = updated;
        this.letterCount = letterCount;
        this.isRunning = isRunning;
        this.id = id;
        this.playersTurn = playersTurn;
        this.lastMove = lastMove;
        this.player = player;
        this.opponent = opponent;
        this.ruleset = ruleset;
    }

    public long getId() {
        return id;
    }

    public Move getLastMove() {
        return lastMove;
    }
}
