package com.webcheckers.model;

//created by Marcus, commented, and cleaned by Beck

public class Player {

    private final String userName;
    public boolean spectating;

    /**
     * Creates a new instance of a Player class
     * @param userName the name that the user signed in with
     */
    public Player (String userName) {
        this.userName = userName;
    }

    /**
     * This function will get and return the name of the player
     * @return the name of the player
     */
    public synchronized String getName() {
        return userName;
    }

    /**
     * This function will return if someone is a spectator
     * @return true if spectator, false if not
     */
    public boolean isSpectating() {
        return spectating;
    }

    /**
     * This will set a player to be a spectator or not
     * @param spectating is a player a spectator?
     */
    public void setSpectating(boolean spectating) {
        this.spectating = spectating;
    }
    
    /**
     * Checks and sees if the provided Object is equal to
     * this instance of a Player class
     * @param obj the object to be compared
     * @return true if equal, false if not
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Player)) return false;
        final Player that = (Player) obj;
        return this.userName.equals(that.userName);
    }

    /**
     * Returns a unique hashcode for the instance of the Player
     * @return the unique hashcode int
     */
    @Override
    public int hashCode() {
        return userName.hashCode();
    }

    /**
     * Creates a String version of the Player's username
     * @return the player's name
     */
    @Override
    public String toString() {
        return this.userName;
    }
}
