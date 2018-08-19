package kimstephenbovim.wordfeudtiles;

import java.util.ArrayList;
import java.util.List;

import kimstephenbovim.wordfeudtiles.domain.Game;
import kimstephenbovim.wordfeudtiles.domain.User;

public class AppData {

    public static AppData shared = new AppData();
    private User user;
    private List<Game> games = new ArrayList<>();

    private AppData() { }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> newGames) {
        ArrayList<Game> mostRecent = new ArrayList<>();
        for (Game newGame : newGames) {
            boolean usedStored = false;
            for (Game storedGame : games) {
                if (newGame.getId() == storedGame.getId()
                    && newGame.getUpdated() <= storedGame.getUpdated()) {
                    usedStored = true;
                    mostRecent.add(storedGame);
                    System.out.println("Store saved game against: " + storedGame.getOpponent().presentableUsername());
                    break;
                }
            }
            if (!usedStored) {
                mostRecent.add(newGame);
                System.out.println("Store new game against: " + newGame.getOpponent().presentableUsername());
            }
        }
        games = mostRecent;
    }

    public void setGame(Game game) {
        for (Game storedGame : games) {
            if (game.getId() == storedGame.getId()
                    && game.getUpdated() >= storedGame.getUpdated()) {
                storedGame.setLetterCount(game.getLetterCount());
                System.out.println("Store letterCount for game against: " + game.getOpponent().presentableUsername());
                break;
            }
        }
    }

    public Game getGame(long gameId) {
        for (Game game: games) {
            if (game.getId() == gameId) {
                return game;
            }
        }
        System.out.println("getGame returns null - this shouldn't happen");
        return null;
    }
}
