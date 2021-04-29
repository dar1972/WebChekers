package com.webcheckers.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

// Created, commented, and cleaned by Beck
public class Game {



    private final Player red;
    private final Player white;
    private final int id;
    private static final AtomicInteger atomicInteger = new AtomicInteger(0); // thread safe.
    private BoardView gameBoardWhite;  //used in other classes, just not here
    private BoardView gameBoardRed;    //used in other classes, just not here
    private final ArrayList<BoardView> snapshotsRed;
    private final ArrayList<BoardView> snapshotsWhite;
    private final ArrayList<BoardView> snapshotsTemp;
    private final ArrayList<Move> moves;
    private String playerWhoResigned;

    public enum ActiveColor {
        RED, WHITE
    }

    private ActiveColor activeColor = ActiveColor.RED;
    private Player winner;
    private boolean isOver;

    /**
     * Create a new instance of a game
     * 
     * @param red   the red player in a game
     * @param white the white player in a game
     */
    public Game(Player red, Player white) {
        this.red = red;
        gameBoardRed = new BoardView("red", true);
        this.white = white;
        gameBoardWhite = new BoardView("white", true);
        this.id = atomicInteger.incrementAndGet();
        playerWhoResigned = "";
        moves = new ArrayList<>();
        snapshotsRed = new ArrayList<>();
        snapshotsRed.add(new BoardView("red", true));
        snapshotsWhite = new ArrayList<>();
        snapshotsWhite.add(new BoardView("white", true));
        snapshotsTemp = new ArrayList<>();
        winner = null;
        isOver = false;
    }

    /**
     * This function will get and return the red player
     * 
     * @return the red player
     */
    public Player getRed() {
        return red;
    }

    /**
     * This function will return if the game is over
     * @return true if over, false if not
     */
    public boolean getIsOver() {
        return isOver;
    }

    /**
     * This function will get and return the white player
     * @return the white player
     */
    public Player getWhite() {
        return white;
    }

    /**
     * This function will set the new white game board
     * @param gameBoardWhite the new board
     */
    public void setGameBoardWhite(BoardView gameBoardWhite) {
        this.gameBoardWhite = gameBoardWhite;
    }

    /**
     * This function will set the new red game board
     * @param gameBoardRed the new board
     */
    public void setGameBoardRed(BoardView gameBoardRed) {
        this.gameBoardRed = gameBoardRed;
    }

    /**
     * This function will return the current active color
     * @return return red or white
     */
    public ActiveColor getActiveColor() {
        return activeColor;
    }

    /**
     * This function will get the currently active player
     * @return the active player
     */
    public Player getActivePlayer(){
        if(activeColor==ActiveColor.RED){
            return red;
        }else{
            return white;
        }
    }

    /**
     * This function will update which player is currently
     * the active player
     */
    public void updateActivePlayer(){
        if(activeColor==ActiveColor.RED){
            activeColor = ActiveColor.WHITE;
        }else{
            activeColor = ActiveColor.RED;
        }
        // since the players changed, the moves made should be removed
        moves.clear();
    }

    /**
     * This function will return the winner of the game
     * @return the Player who won
     */
    public Player getWinner() {
        return winner;
    }

    /**
     * This function will set the player who won
     * @param player the player who won
     */
    public void setWinner(Player player) {
        this.winner = player;
        this.isOver = true;
    }

    /**
     * This function will return if a winner exists
     * @return true if there is a winner, false if not
     */
    public boolean isWinner(){
        return winner != null;
    }

    /**
     * This function will back up to the move that was done
     * before the current
     * @return true if backup is possible, false if not
     */
    public boolean backupMove() {
        // set up the comparisons
        int size1 = getActiveSnapshots().size();
        int size2 = getTempSnapshots().size();
        getActiveSnapshots().remove(getActiveSnapshots().size()-1);
        getTempSnapshots().remove(getTempSnapshots().size()-1);

        resetMoveAllowed();

        // return if the backup is possible based on number of moves done
        return getActiveSnapshots().size() < size1 && getTempSnapshots().size() < size2;

    }

    /**
     * This function will return the game ID
     * @return the game ID
     */
    public int getGameId() {
        return id;
    }

    /**
     * This function will get the moves done
     * @return an Arraylist of the moves done
     */
    public ArrayList<Move> getMoves(){
        return moves;
    }

    /**
     * This function will store the move defined in the list of
     * done moves
     * @param move the move to be stored
     */
    public void storeMove(Move move){

        // add the move to the made moves
        moves.add(move);
        ArrayList<BoardView> activeSnapshots = getActiveSnapshots();
        ArrayList<BoardView> inactiveSnapshots = getInactiveSnapshots();

        BoardView activeBoard = copyBoard(activeSnapshots.get(getActiveSnapshots().size()-1));

        BoardView tempBoard;
        // check if the size of the temp snap is 0
        if (snapshotsTemp.size() == 0) {
            tempBoard = copyBoard(inactiveSnapshots.get(getInactiveSnapshots().size()-1));
        }

        // if not zero
        else {
            tempBoard = copyBoard(snapshotsTemp.get(snapshotsTemp.size()-1));
        }

        Move invertedMove = move.invertMove();

        activeBoard.updateBoard(move);
        tempBoard.updateBoard(invertedMove);

        activeSnapshots.add(activeBoard);

        //store in temporary board so the inactive player doesn't get their board prematurely refreshed
        snapshotsTemp.add(tempBoard);
    }

    /**
     * This function will copy the board inputted
     * @param object the board to be copied
     * @return the new board
     */
    public BoardView copyBoard(Object object) {
        try {
          ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
          ObjectOutputStream outputStrm = new ObjectOutputStream(outputStream);
          outputStrm.writeObject(object);
          ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
          ObjectInputStream objInputStream = new ObjectInputStream(inputStream);
          return (BoardView) objInputStream.readObject();
        }
        catch (Exception e) {
          e.printStackTrace();
          return null;
        }
      }

    /**
     * This function will get the ActiveSnapshots
     * @return the ActiveSnapshots
     */
    public ArrayList<BoardView> getActiveSnapshots(){
        if (activeColor == ActiveColor.RED) {
            return snapshotsRed;
        }
        else {
            return snapshotsWhite;
        }
    }

    /**
     * This function will get the InactiveSnapshots
     * @return the InactiveSnapshots
     */
    public ArrayList<BoardView> getInactiveSnapshots(){
        if (activeColor == ActiveColor.RED) {
            return snapshotsWhite;
        }
        else {
            return snapshotsRed;
        }
    }

    /**
     * This function will get the TempSnapshots
     * @return the TempSnapshots
     */
    public ArrayList<BoardView> getTempSnapshots() {
        return snapshotsTemp;
    }

    /**
     * This function will get the RedSnapshots
     * @return the RedSnapshots
     */
    public ArrayList<BoardView> getRedSnapshots(){ return snapshotsRed; }

    /**
     * This function will get the WhiteSnapshots
     * @return the WhiteSnapshots
     */
    public ArrayList<BoardView> getWhiteSnapshots(){ return snapshotsWhite; }

    /**
     * This function will reset all of the moves allowed
     */
    public void resetMoveAllowed() {
        ArrayList<BoardView> redSnapshots = getRedSnapshots();
        ArrayList<BoardView> whiteSnapshots = getWhiteSnapshots();
        redSnapshots.get(redSnapshots.size()-1).setMoveAllowed(true);
        whiteSnapshots.get(whiteSnapshots.size()-1).setMoveAllowed(true);
    }

    /**
     * This will set the player who resigned
     * @param playerWhoResigned the player who resigned
     */
    public void setPlayerWhoResigned(String playerWhoResigned) {
        this.playerWhoResigned = playerWhoResigned;
    }

    /**
     * This will return the player who resigned
     * @return the player who resigned
     */
    public String getPlayerWhoResigned() {
        return playerWhoResigned;
    }

    /**
     * This function will check if the moves of a color was cleared
     * @return this will return if the active player's moves were cleared
     */
    public boolean colorCleared() {
        ArrayList<BoardView> snapshots = getActiveSnapshots();
        BoardView board = snapshots.get(snapshots.size()-1);

        return board.colorCleared();
    }
    
    /**
     * This function will check to see if the selected Object is equal
     * to this Game instance
     * @param obj the object to be compared
     * @return true if equal, false if not
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Game)) return false;
        final Game that = (Game) obj;
        return this.id == that.id;
    }
}
