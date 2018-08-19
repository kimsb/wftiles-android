package kimstephenbovim.wordfeudtiles.event;

import kimstephenbovim.wordfeudtiles.domain.Game;

public class GameLoadedEvent {

    private final Game game;

    public GameLoadedEvent(final Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
