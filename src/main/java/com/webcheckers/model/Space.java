package com.webcheckers.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Space implements Serializable {

    //Created, commented, and cleaned by Beck

    private final int cellIdx;
    private final String color;
    private Piece piece;

    /**
     * Constructor for a new space
     * @param cellIdx the index in a row that the piece is
     * @param color the color of the space (WHITE/BLACK)
     */
    public Space(int cellIdx, String color, String pieceColor){
        this.cellIdx = cellIdx;
        this.color = color;
        //create a new piece on black spaces for the start of the game
        if(color.equals("Black")){
            if(!(pieceColor.equals("q"))){
                piece = new Piece("s", pieceColor);
            }
        }
        // if not a black space, no piece exists
        else{
            piece = null;
        }
    }

    /**
     * Gets the cell index of the space
     * @return the cell index
     */
    public int getCellIdx(){
        return cellIdx;
    }

    /**
     * checks to see if the space is a valid position for a piece
     * @return true if it is and false if not
     */
    public boolean isValid(){
        return color.equals("Black");
    }

    /**
     * Checks to see if a piece is on the space
     * @return the piece is there is one, null if not
     */
    public Piece getPiece(){
        return piece;
    }

    /**
     * This will set a piece on the space
     * @param newPiece the piece to be on 
     */
    public void setPiece(Piece newPiece){
        this.piece = newPiece;
    }

    /**
     * This function will create a new piece on the space
     * @param type the type of piece
     * @param color the color of the piece
     */
    public void createPiece(String type, String color){
        piece = new Piece(type,color);
    }

    /**
     * This function will delete the piece on the space
     */
    public void deletePiece(){
        if(piece!=null){
            piece=null;
        }
    }
}
