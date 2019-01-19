package kimstephenbovim.wordfeudtiles;

import android.app.Application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kimstephenbovim.wordfeudtiles.domain.Game;
import kimstephenbovim.wordfeudtiles.domain.Preferences;
import kimstephenbovim.wordfeudtiles.domain.User;

public class WFTiles extends Application {

    private static String INTERNAL_USERS = "internal_users";
    private static String INTERNAL_GAMES_MAP = "internal_games_map";
    private static String INTERNAL_PREFERENCES = "internal_preferences";

    public static WFTiles instance;
    private User lastAttemptedLogin;
    private List<User> users = new ArrayList<>();
    private Map<Long, List<Game>> games = new HashMap<>();
    private Preferences preferences;

    public WFTiles() {
        instance = this;
    }

    public void logoutCurrentUser() {
        if (!users.isEmpty()) {
            User remove = users.remove(0);
            writeToFile(INTERNAL_USERS, users);
            lastAttemptedLogin = users.isEmpty() ? remove : null;
        }
    }

    public User getLoggedInUser() {
        List<User> users = getUsers();
        if (users.isEmpty()) {
            Object readObject = readFromFile("internal_user");
            if (readObject instanceof User) {
                User user = (User) readObject;
                addUser(user);
                writeToFile("internal_user", null);
                return user;
            }
            return null;
        }
        return users.get(0);
    }

    public List<User> getUsers() {
        if (users.isEmpty()) {
            Object readObject = readFromFile(INTERNAL_USERS);
            if (readObject instanceof List) {
                users = (List) readObject;
                System.out.println("Gets users from internal storage");
            }
        }
        return users;
    }

    public void addUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == user.getId()) {
                users.remove(i);
            }
        }
        users.add(0, user);
        if (users.size() > 10) {
            users.remove(10);
        }
        writeToFile(INTERNAL_USERS, users);
    }

    public Preferences getPreferences() {
        if (preferences == null) {
            Object readObject = readFromFile(INTERNAL_PREFERENCES);
            if (readObject instanceof Preferences) {
                preferences = (Preferences) readObject;
            }
        }
        return preferences == null
                ? new Preferences()
                : preferences;
    }

    private void setPreferences(Preferences preferences) {
        this.preferences = preferences;
        writeToFile(INTERNAL_PREFERENCES, preferences);
    }

    public void setPreferredLocaleIndex(int preferredLocaleIndex) {
        Preferences preferences = getPreferences();
        preferences.setLocaleIndex(preferredLocaleIndex);
        setPreferences(preferences);
    }

    public void setPreferredView(boolean viewOverview) {
        Preferences preferences = getPreferences();
        preferences.setViewOverview(viewOverview);
        setPreferences(preferences);
    }

    public void setPreferredSorting(boolean sortVowelsConsonants) {
        Preferences preferences = getPreferences();
        preferences.setSortVowelsConsonants(sortVowelsConsonants);
        setPreferences(preferences);
    }

    public List<Game> getGames() {
        User loggedInUser = getLoggedInUser();
        if (loggedInUser == null) {
            return new ArrayList<>();
        }
        if (!games.containsKey(loggedInUser.getId())) {
            Object readObject = readFromFile(INTERNAL_GAMES_MAP);
            if (readObject instanceof Map) {
                games = (Map) readObject;
                System.out.println("Gets games_map from internal storage");
            }
        }
        if (!games.containsKey(loggedInUser.getId())) {
            Object readObject = readFromFile("internal_games");
            if (readObject instanceof List) {
                games.put(loggedInUser.getId(), (List) readObject);
                System.out.println("Gets games from internal storage");
            }
        }
        return games.containsKey(loggedInUser.getId())
                ? games.get(loggedInUser.getId())
                : new ArrayList<Game>();
    }

    public void setGames(List<Game> newGames) {
        ArrayList<Game> mostRecent = new ArrayList<>();
        for (Game newGame : newGames) {
            boolean foundStored = false;
            for (Game storedGame : getGames()) {
                if (newGame.getId() == storedGame.getId()
                        && newGame.getUpdated() == storedGame.getUpdated()) {
                    mostRecent.add(storedGame);
                    //TODO ser ikke ut til å komme hit
                    System.out.println("Store saved game against: " + storedGame.getOpponent().presentableUsername());
                    foundStored = true;
                    break;
                }
            }
            if (!foundStored) {
                mostRecent.add(newGame);
                System.out.println("Store new game against: " + newGame.getOpponent().presentableUsername());
            }
        }
        games.put(getLoggedInUser().getId(), mostRecent);
        writeToFile(INTERNAL_GAMES_MAP, games);
    }

    public boolean gameIsNewOrUpdated(final Game game) {
        for (Game storedGame : getGames()) {
            if (game.getId() == storedGame.getId()
                    && game.getUpdated() == storedGame.getUpdated()
                    && storedGame.getRemainingLetters() != null
                    && game.getRemainingLetters().size() == storedGame.getRemainingLetters().size()) {
                return false;
            }
        }
        return true;
    }

    public void setGame(Game game) {
        for (Game storedGame : getGames()) {
            if (game.getId() == storedGame.getId()) {
                storedGame.setRemainingLetters(game.getRemainingLetters());
                System.out.println("Store letterCount for game against: " + game.getOpponent().presentableUsername());
                writeToFile(INTERNAL_GAMES_MAP, games);
                break;
            }
        }
    }

    public Game getGame(long gameId) {
        for (Game game : getGames()) {
            if (game.getId() == gameId) {
                return game;
            }
        }
        return null;
    }

    //TODO egen lagring av passord
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
        } catch (FileNotFoundException e) {
            System.out.println("Filen: " + filename + " er ikke opprettet ennå.");
            return null;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getLastAttemptedLogin() {
        return lastAttemptedLogin;
    }

    public void setLastAttemptedLogin(User user) {
        lastAttemptedLogin = user;
    }
}
