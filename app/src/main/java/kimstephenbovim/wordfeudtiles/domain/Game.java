package kimstephenbovim.wordfeudtiles.domain;

import java.util.Map;

import kimstephenbovim.wordfeudtiles.AppData;
import kimstephenbovim.wordfeudtiles.Texts;

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

    public Player getOpponent() {
        return opponent;
    }

    public int getRuleset() {
        return ruleset;
    }

    public Player getPlayer() {
        return player;
    }

    public String getLastMoveText() {
        final String opponentName = opponent.presentableUsername();
        if (lastMove == null) {
            if (playersTurn) {
                return String.format(Texts.shared.getText("firstMoveYou"), opponentName);
            } else {
                return String.format(Texts.shared.getText("firstMoveThem"), opponentName);
            }
        }
        switch (lastMove.getMoveType()) {
            case "move":
                if (lastMove.getUserId() == AppData.shared.getUser().getId()) {
                    return String.format(Texts.shared.getText("youPlayed"), lastMove.getMainWord(), lastMove.getPoints());
                } else {
                    return String.format(Texts.shared.getText("theyPlayed"), opponentName, lastMove.getMainWord(), lastMove.getPoints());
                }
            case "pass":
                if (lastMove.getUserId() == AppData.shared.getUser().getId()) {
                    return Texts.shared.getText("youPassed");
                } else {
                    return String.format(Texts.shared.getText("theyPassed"), opponentName);

                }
            case "swap":
                if (lastMove.getUserId() == AppData.shared.getUser().getId()) {
                    if (lastMove.getTileCount() == 1) {
                        return String.format(Texts.shared.getText("youSwappedOne"), lastMove.getTileCount());
                    }
                    return String.format(Texts.shared.getText("youSwapped"), lastMove.getTileCount());
                } else {
                    if (lastMove.getTileCount() == 1) {
                        return String.format(Texts.shared.getText("theySwappedOne"), opponentName, lastMove.getTileCount());
                    }
                    return String.format(Texts.shared.getText("theySwapped"), opponentName, lastMove.getTileCount());
                }
            case "resign":
                if (lastMove.getUserId() == AppData.shared.getUser().getId()) {
                    return Texts.shared.getText("youResigned");
                } else {
                    return String.format(Texts.shared.getText("theyResigned"), opponentName);
                }
            default:
                return "";
        }
    }

    public Map<String, Integer> getLetterCount() {
        return letterCount;
    }

    public long getUpdated() {
        return updated;
    }

    public void setLetterCount(Map<String, Integer> letterCount) {
        this.letterCount = letterCount;
    }
}
