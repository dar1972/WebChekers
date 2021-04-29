package com.webcheckers.appl;

import java.util.HashMap;

import com.webcheckers.model.Player;

public class PlayerLobby {
    //created by Marcus, commented, and cleaned by Beck

    HashMap<String, Player> players = new HashMap<>();

    /**
     * This function adds a player to the lobby
     * @param user the user to be added
     * @return true if player added, false if player with name
     *            already exists
     */
	public synchronized boolean addToLobby(Player user) {
        if (players.containsKey(user.getName())) {
            return false;
        } else {
            players.put(user.getName(), user);
            return true;
        }
    }

    /**
     * This function will remove the provided player from the lobby
     * @param user the user to be removed
     */
    public synchronized void removeFromLobby(Player user) {
        players.remove(user.getName());
    }

    /**
     * This function will check if a player's username is valid
     */
    public synchronized boolean isValid(String userName) {
        // checks if the characters used are letters
        boolean isAlpha = !userName.matches("^.*[^a-zA-Z0-9].*$");

        // makes sure that the name isn't blank
        if (userName.equals("")) {
            isAlpha = false;
        }
        return isAlpha;
    }

    /**
     * This function get the players in the lobby
     * @return the hashmap with the players
     */
    public HashMap<String, Player> getPlayers() {
        return players;
    }
    
}
