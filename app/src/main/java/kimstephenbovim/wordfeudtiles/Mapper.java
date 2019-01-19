package kimstephenbovim.wordfeudtiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import kimstephenbovim.wordfeudtiles.domain.Game;
import kimstephenbovim.wordfeudtiles.domain.GameRow;
import kimstephenbovim.wordfeudtiles.domain.Move;
import kimstephenbovim.wordfeudtiles.domain.Player;
import kimstephenbovim.wordfeudtiles.domain.User;
import kimstephenbovim.wordfeudtiles.rest.GameDTO;
import kimstephenbovim.wordfeudtiles.rest.LoginContent;
import kimstephenbovim.wordfeudtiles.rest.MoveDTO;
import kimstephenbovim.wordfeudtiles.rest.PlayerDTO;

import static java.util.Arrays.asList;

public class Mapper {

    public static Game mapToGame(final GameDTO gameDTO) {
        boolean playersTurn = gameDTO.getPlayers().get(gameDTO.getCurrentPlayer()).getId() == WFTiles.instance.getLoggedInUser().getId();

        Player loggedInPlayer, opponent;
        if (gameDTO.getPlayers().get(0).getId() == WFTiles.instance.getLoggedInUser().getId()) {
            loggedInPlayer = mapToPlayer(gameDTO.getPlayers().get(0), gameDTO.getRuleset());
            opponent = mapToPlayer(gameDTO.getPlayers().get(1), gameDTO.getRuleset());
        } else {
            loggedInPlayer = mapToPlayer(gameDTO.getPlayers().get(1), gameDTO.getRuleset());
            opponent = mapToPlayer(gameDTO.getPlayers().get(0), gameDTO.getRuleset());
        }

        List<String> remainingLetters = mapUsedLettersToRemaining(gameDTO.getRuleset(),
                mapTilesToUsedLetters(gameDTO.getTiles()), loggedInPlayer.getRack());

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

    public static List<Game> mapToGames(final List<GameDTO> gameDTOs) {
        ArrayList<Game> games = new ArrayList<>();
        for (GameDTO gameDTO : gameDTOs) {
            games.add(mapToGame(gameDTO));
        }
        return games;
    }

    static Player mapToPlayer(final PlayerDTO playerDTO, final int ruleset) {
        return new Player(playerDTO.getUsername(),
                playerDTO.getId(),
                playerDTO.getScore(),
                playerDTO.getAvatarUpdated(),
                playerDTO.getFbFirstName(),
                playerDTO.getFbMiddleName(),
                playerDTO.getFbLastName(),
                rackOrderedByRuleset(playerDTO.getRack(), ruleset),
                ruleset);
    }

    private static List<String> rackOrderedByRuleset(List<String> rack, int ruleset) {
        if (rack == null) {
            return null;
        }
        final List<String> letterOrder = asList(Constants.shared.getLetters(ruleset));
        Collections.sort(rack, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int x = letterOrder.indexOf(o1);
                int y = letterOrder.indexOf(o2);
                return (x < y) ? -1 : ((x == y) ? 0 : 1);
            }
        });
        return rack;
    }

    static Move mapToMove(final MoveDTO moveDTO) {
        return moveDTO == null
                ? null
                : new Move(moveDTO.getPoints(),
                moveDTO.getMoveType(),
                moveDTO.getUserId(),
                moveDTO.getMainWord(),
                moveDTO.getTileCount());
    }

    public static User mapToUser(final LoginContent loginContent, final String loginMethod, final String password) {
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

    private static List<String> mapUsedLettersToRemaining(int ruleset, List<String> usedLetters, List<String> rack) {
        if (usedLetters == null) {
            return null;
        }
        if (Texts.shared.unsupportedLanguage(ruleset)) {
            return new ArrayList<>();
        }
        Map<String, Integer> letterCount = Constants.shared.getCounts(ruleset);
        for (String letter : usedLetters) {
            letterCount.put(letter, letterCount.get(letter) - 1);
        }
        for (String letter : rack) {
            letterCount.put(letter, letterCount.get(letter) - 1);
        }
        ArrayList<String> remainingLetters = new ArrayList<>();
        for (String letter : Constants.shared.getLetters(ruleset)) {
            for (int i = 0; i < letterCount.get(letter); i++) {
                remainingLetters.add(letter);
            }
        }
        return remainingLetters;
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

    static List<GameRow> mapGamesToGameRows(List<Game> games) {
        ArrayList<GameRow> yourTurn = new ArrayList<>();
        ArrayList<GameRow> theirTurn = new ArrayList<>();
        ArrayList<GameRow> finished = new ArrayList<>();

        for (Game game : games) {
            if (!game.getIsRunning()) {
                finished.add(new GameRow(game));
            } else {
                if (game.getPlayersTurn()) {
                    yourTurn.add(new GameRow(game));
                } else {
                    theirTurn.add(new GameRow(game));
                }
            }
        }
        if (!yourTurn.isEmpty()) {
            yourTurn.add(0, new GameRow(Texts.shared.getText("yourTurn")));
        }
        if (!theirTurn.isEmpty()) {
            theirTurn.add(0, new GameRow(Texts.shared.getText("opponentsTurn")));
        }
        if (!finished.isEmpty()) {
            finished.add(0, new GameRow(Texts.shared.getText("finishedGames")));
        }
        ArrayList<GameRow> gameRows = new ArrayList<>(yourTurn);
        gameRows.addAll(theirTurn);
        gameRows.addAll(finished);
        return gameRows;
    }
}
