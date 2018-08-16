package kimstephenbovim.wordfeudtiles.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kimstephenbovim.wordfeudtiles.AppData;
import kimstephenbovim.wordfeudtiles.Constants;
import kimstephenbovim.wordfeudtiles.Texts;
import kimstephenbovim.wordfeudtiles.domain.Game;
import kimstephenbovim.wordfeudtiles.domain.Move;
import kimstephenbovim.wordfeudtiles.domain.Player;
import kimstephenbovim.wordfeudtiles.domain.User;

public class Mapper {

    static Game mapToGame(final GameDTO gameDTO) {
        boolean playersTurn = gameDTO.getPlayers().get(gameDTO.getCurrentPlayer()).getId() == AppData.shared.getUser().getId();

        Player loggedInPlayer, opponent;
        if (gameDTO.getPlayers().get(0).getId() == AppData.shared.getUser().getId()) {
            loggedInPlayer = mapToPlayer(gameDTO.getPlayers().get(0));
            opponent = mapToPlayer(gameDTO.getPlayers().get(1));
        } else {
            loggedInPlayer = mapToPlayer(gameDTO.getPlayers().get(1));
            opponent = mapToPlayer(gameDTO.getPlayers().get(0));
        }

        Map<String, Integer> remainingLetters = mapUsedLettersToRemaining(gameDTO.getRuleset(),
                mapTilesToUsedLetters(gameDTO.getTiles()), loggedInPlayer.getRack());

        //TODO sort rack
        return new Game(gameDTO.getUpdated(),
                remainingLetters,
                gameDTO.getRunning(),
                gameDTO.getId(),
                playersTurn,
                mapToMove(gameDTO.getLastMove()),
                loggedInPlayer,
                opponent,
                gameDTO.getRuleset());
    }

    static List<Game> mapToGames(final GamesContent gamesContent) {
        ArrayList<Game> games = new ArrayList<>();
        for (GameDTO gameDTO : gamesContent.getGames()) {
            games.add(mapToGame(gameDTO));
        }
        return games;
    }

    static Player mapToPlayer(final PlayerDTO playerDTO) {
        return new Player(playerDTO.getUsername(),
                playerDTO.getId(),
                playerDTO.getScore(),
                playerDTO.getAvatarUpdated(),
                playerDTO.getFbFirstName(),
                playerDTO.getFbMiddleName(),
                playerDTO.getFbLastName());
    }

    static Move mapToMove(final MoveDTO moveDTO) {
        return new Move(moveDTO.getPoints(),
                moveDTO.getMoveType(),
                moveDTO.getUserId(),
                moveDTO.getMainWord(),
                moveDTO.getTileCount());
    }

    static User mapToUser(final LoginContent loginContent, final String password, final String loginMethod) {
        return new User(loginContent.getUsername(),
                loginContent.getEmail(),
                password,
                loginContent.getId(),
                loginContent.getAvatarRoot(),
                loginMethod,
                loginContent.getFbFirstName(),
                loginContent.getFbMiddleName(),
                loginContent.getFbLastName());
    }

    private static Map<String, Integer> mapUsedLettersToRemaining(int ruleset, List<String> usedLetters, List<String> rack) {
        if (usedLetters == null || Texts.shared.unsupportedLanguage(ruleset)) {
            return new HashMap<>();
        }
        Map<String, Integer> letterCount = Constants.shared.getCounts(ruleset);
        for (String letter : usedLetters) {
            letterCount.put(letter, letterCount.get(letter) - 1);
        }
        for (String letter : rack) {
            letterCount.put(letter, letterCount.get(letter) - 1);
        }
        return letterCount;
    }

    private static List<String> mapTilesToUsedLetters(List<List<Object>> tiles) {
        if (tiles == null) {
            return null;
        }
        ArrayList<String> usedLetters = new ArrayList<>();
        for (List<Object> objectList : tiles) {
            if ((Boolean) objectList.get(3)) {
                usedLetters.add("");
            } else {
                usedLetters.add((String) objectList.get(2));
            }
        }
        return usedLetters;
    }
}
