package com.webcheckers.appl;

import java.util.HashMap;
import java.util.Map;

import com.webcheckers.model.Game;
import com.webcheckers.model.Move;
import com.webcheckers.model.Player;

public class GameCenter {

    //Created, commented, and cleaned by Beck

    private final PlayerLobby playerLobby;
    private final HashMap<String, Game> gameLobby;
    private final HashMap<Integer, Game> gameArchive;

    /**
     * This function will create a new instance of GameCenter
     * @param playerLobby the player lobby for the server
     */
    public GameCenter(PlayerLobby playerLobby) {
        this.playerLobby = playerLobby;
        gameLobby = new HashMap<>();
        gameArchive = new HashMap<>();
    }

    /**
     * This function will create a new game instance
     * @param redName The red player
     * @param whiteName The white player
     */
    public synchronized void createGame(String redName, String whiteName) {

        // get the players
        Player red = playerLobby.getPlayers().get(redName);
        Player white = playerLobby.getPlayers().get(whiteName);

        // create the new game
        Game game = new Game(red, white);

        // put the game in the gameLobby game list
        gameLobby.put(redName, game);
        gameLobby.put(whiteName, game);
        gameArchive.put(game.getGameId(), game);

    }

    /**
     * This function will get the game lobby
     * @return the gamelobby
     */
    public Map<String, Game> getGameLobby() {
        return gameLobby;
    }

    /**
     * This function will get the game with the given player
     * @param playerName the player in the game wanted
     * @return game with the player
     */
    public Game getGame(String playerName) {
        return gameLobby.get(playerName);
    }

    /**
     * This function will get the game with the given ID
     * @param gameID the ID of the game wanted
     * @return the game
     */
    public Game getGame(int gameID) {
        return gameArchive.get(gameID);
    }

    /**
     * This function will get the opponent of the given player
     * @param user the player who's opponent is wanted
     * @return the player's opponent
     */
    public Player getOpponent(Player user) {

        // This will get the game with the wanted player
        String userName = user.getName();
        Game game = gameLobby.get(userName);

        // get the player who's color is the opposite of the provided player
        if (user == game.getRed()) {
            return game.getWhite();
        }
        else {
            return game.getRed();
        }
    }

    /**
     * This function will check if the player is Red
     * @param user the player who's color is wanted
     * @return true if red, false if white
     */
    public synchronized boolean isRed(Player user) {
        // get the game of the player
        String userName = user.getName();
        Game game = gameLobby.get(userName);
        // true if red, false if not
        return game.getRed().equals(user);
    }

    /**
     * This function will check if the provided player is active
     * @param name the name of the player
     * @return true if active, false if not
     */
    public boolean isPlayerActive(String name){
        // get the game of the player
        Game game = getGame(name);
        // return true if active player is the provided
        if(name!=null && game!=null){
            return name.equals(game.getActivePlayer().getName());
        }return false;
    }

    /**
     * This function will check if a player is in a game
     * @param name the name of the checked player
     * @return true if in a game, false if not
     */
    public synchronized boolean isPlayerInGame(String name){
        return gameLobby.containsKey(name);
    }

    /**
     * This function will finish the turn of the game with
     * the provided player
     * @param name the name of the player who's game is wanted
     * @return true if game exists, false if not
     */
    public boolean finishTurn(String name){
        Game game = getGame(name);
        boolean exists = false;
        if(game!=null){
            exists = true;
        }
        return exists;
    }

    /**
     * This function will set a winner and remove a player from a lobby
     * @param user the player to be removed
     */
    public synchronized void leaveGame(Player user) {
        getGame(user.getName()).setWinner(getOpponent(user));
        gameLobby.remove(user.getName());
    }

    /**
     * This function will store a move in the done moved
     * @param move The move to be stored
     * @param game The game the move will be stored in
     */
    public void storeMove(Move move, Game game){
        game.storeMove(move);
    }

    /**
     * This function will get the archived games
     * @return a hashmap of the archived games
     */
	public HashMap<Integer, Game> getArchivedGames() {
		return gameArchive;
    }
}
