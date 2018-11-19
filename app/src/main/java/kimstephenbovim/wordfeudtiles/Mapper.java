package kimstephenbovim.wordfeudtiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kimstephenbovim.wordfeudtiles.domain.Game;
import kimstephenbovim.wordfeudtiles.domain.GameDetailItem;
import kimstephenbovim.wordfeudtiles.domain.GameRow;
import kimstephenbovim.wordfeudtiles.domain.Move;
import kimstephenbovim.wordfeudtiles.domain.Player;
import kimstephenbovim.wordfeudtiles.domain.User;
import kimstephenbovim.wordfeudtiles.rest.GameDTO;
import kimstephenbovim.wordfeudtiles.rest.LoginContent;
import kimstephenbovim.wordfeudtiles.rest.MoveDTO;
import kimstephenbovim.wordfeudtiles.rest.PlayerDTO;

import static kimstephenbovim.wordfeudtiles.domain.GameDetailType.TILE;
import static kimstephenbovim.wordfeudtiles.domain.GameDetailType.TILE_SUMMARY;

public class Mapper {

    public static Game mapToGame(final GameDTO gameDTO) {
        boolean playersTurn = gameDTO.getPlayers().get(gameDTO.getCurrentPlayer()).getId() == WFTiles.instance.getUser().getId();

        Player loggedInPlayer, opponent;
        if (gameDTO.getPlayers().get(0).getId() == WFTiles.instance.getUser().getId()) {
            loggedInPlayer = mapToPlayer(gameDTO.getPlayers().get(0));
            opponent = mapToPlayer(gameDTO.getPlayers().get(1));
        } else {
            loggedInPlayer = mapToPlayer(gameDTO.getPlayers().get(1));
            opponent = mapToPlayer(gameDTO.getPlayers().get(0));
        }

        //TODO usikker på hvor brikkene sorteres, men ÆØÅ sorteres ikke riktig...
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

    public static List<Game> mapToGames(final List<GameDTO> gameDTOs) {
        ArrayList<Game> games = new ArrayList<>();
        for (GameDTO gameDTO : gameDTOs) {
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
                playerDTO.getFbLastName(),
                playerDTO.getRack());
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

    public static List<GameRow> mapGamesToGameRows(List<Game> games) {
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

    public static List<GameDetailItem> mapStringsToTileItems(final List<String> strings) {
        ArrayList<GameDetailItem> gameDetailItems = new ArrayList<>();
        for (String string : strings) {
            gameDetailItems.add(new GameDetailItem(TILE, string));
        }
        return gameDetailItems;
    }

    public static List<GameDetailItem> getTileSummaryItems(final int ruleset) {
        ArrayList<GameDetailItem> gameDetailItems = new ArrayList<>();
        String[] letters = Constants.shared.getLetters(ruleset);
        for (String letter : letters) {
            gameDetailItems.add(new GameDetailItem(TILE_SUMMARY, letter));
        }
        return gameDetailItems;
    }
}
