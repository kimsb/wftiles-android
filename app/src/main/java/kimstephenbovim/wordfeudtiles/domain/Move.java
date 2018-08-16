package kimstephenbovim.wordfeudtiles.domain;

public class Move {
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
}
