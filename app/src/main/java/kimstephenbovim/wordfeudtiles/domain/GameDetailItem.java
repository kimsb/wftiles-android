package kimstephenbovim.wordfeudtiles.domain;

public class GameDetailItem {
    private String itemText;
    private GameDetailType gameDetailType;

    public GameDetailItem(GameDetailType gameDetailType, String itemText) {
        this.gameDetailType = gameDetailType;
        this.itemText = itemText;
    }

    public String getItemText() {
        return itemText;
    }

    public GameDetailType getGameDetailType() {
        return gameDetailType;
    }
}
