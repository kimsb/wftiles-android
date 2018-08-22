package kimstephenbovim.wordfeudtiles.domain;

import java.io.Serializable;

public class Move implements Serializable {
    private Integer points;
    private String moveType;
    private long userId;
    private String mainWord;
    private Integer tileCount;

    public Move(Integer points, String moveType, long userId, String mainWord, Integer tileCount) {
        this.points = points;
        this.moveType = moveType;
        this.userId = userId;
        this.mainWord = mainWord;
        this.tileCount = tileCount;
    }

    public String getMainWord() {
        return mainWord;
    }

    public String getMoveType() {
        return moveType;
    }

    public long getUserId() {
        return userId;
    }

    public Integer getPoints() {
        return points;
    }

    public Integer getTileCount() {
        return tileCount;
    }
}
