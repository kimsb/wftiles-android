package kimstephenbovim.wordfeudtiles.domain;

import static kimstephenbovim.wordfeudtiles.domain.GameRowType.GAME;
import static kimstephenbovim.wordfeudtiles.domain.GameRowType.HEADER;

public class GameRow {
    private String headerTitle;
    private Game game;
    private GameRowType gameRowType;

    public GameRow(final String headerTitle) {
        this.headerTitle = headerTitle;
        gameRowType = HEADER;
    }

    public GameRow(final Game game) {
        this.game = game;
        gameRowType = GAME;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public Game getGame() {
        return game;
    }

    public GameRowType getGameRowType() {
        return gameRowType;
    }
}
