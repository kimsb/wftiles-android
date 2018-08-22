package kimstephenbovim.wordfeudtiles;

import android.app.Application;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import kimstephenbovim.wordfeudtiles.domain.Game;
import kimstephenbovim.wordfeudtiles.domain.User;

public class WFTiles extends Application {

    private static String INERTNAL_USER = "internal_user";
    private static String INTERNAL_GAMES = "internal_games";

    public static WFTiles instance;
    private User user;
    private List<Game> games = new ArrayList<>();

    public WFTiles() {
        instance = this;
    }

    public User getUser() {
        if (user == null) {
            Object readObject = readFromFile(INERTNAL_USER);
            if (readObject instanceof User) {
                user = (User) readObject;
            }
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        writeToFile(INERTNAL_USER, user);
    }

    public List<Game> getGames() {
        if (games.isEmpty()) {
            Object readObject = readFromFile(INTERNAL_GAMES);
            if (readObject instanceof List) {
                games = (List) readObject;
                System.out.println("Gets games from internal storage");
            }
        }
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
        writeToFile(INTERNAL_GAMES, games);
    }

    public void setGame(Game game) {
        for (Game storedGame : games) {
            if (game.getId() == storedGame.getId()
                    && game.getUpdated() >= storedGame.getUpdated()) {
                storedGame.setLetterCount(game.getLetterCount());
                System.out.println("Store letterCount for game against: " + game.getOpponent().presentableUsername());
                writeToFile(INTERNAL_GAMES, games);
                break;
            }
        }
    }

    public Game getGame(long gameId) {
        for (Game game : games) {
            if (game.getId() == gameId) {
                return game;
            }
        }
        System.out.println("getGame returns null - this shouldn't happen");
        return null;
    }

    private void writeToFile(String filename, Object object) {
        try {
            FileOutputStream fileOutputStream = openFileOutput(filename, MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Object readFromFile(String filename) {
        try {
            FileInputStream fileInputStream = openFileInput(filename);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Object readObject = objectInputStream.readObject();
            objectInputStream.close();
            return readObject;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
