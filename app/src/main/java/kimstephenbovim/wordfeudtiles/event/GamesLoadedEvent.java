package kimstephenbovim.wordfeudtiles.event;

import java.util.List;

import kimstephenbovim.wordfeudtiles.domain.Game;

public class GamesLoadedEvent {

    private final List<Game> games;

    public GamesLoadedEvent(final List<Game> games) {
        this.games = games;
    }

    public List<Game> getGames() {
        return games;
    }
}
