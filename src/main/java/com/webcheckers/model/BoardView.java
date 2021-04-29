package com.webcheckers.model;

import com.webcheckers.model.Piece.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings("serial")
public class BoardView implements Iterable<Row>, Serializable{

    //Created, commented, and cleaned by Beck

    public Row[] gameBoard;
    private boolean moveAllowed;

    /**
     * Creates a new board
     * @param color the color of the player who will see the board
     */
    public BoardView(String color, boolean moveAllowed){

        // if movement is possible for the board
        this.moveAllowed = moveAllowed;
        // create the empty list of the rows
        gameBoard = new Row[8];
        Row row;
        // temp color that will never exist for valid space
        String pieceColor = "q";
        int ROWS = 8;

        // for each row on the board
        for (int i = 0; i < ROWS; i ++){
            //for rows 0,1,and 2, white pieces if white player, red if red
            if(i==0||i==1||i==2){
                if(color.equals("white")) {
                    pieceColor = "r";
                }else{
                    pieceColor = "w";
                }
            }
            //for rows 5,6, and 7 red pieces if white player, white if red
            else if(i==7||i==6||i==5){
                if(color.equals("white")) {
                    pieceColor = "w";
                }else{
                    pieceColor = "r";
                }
            }
            //for rows 4 and 5, no pieces
            else if(i==3){
                pieceColor = "q";
            }
            //for rows 0, 2, 4, and 6, the rows will start with white
            if (i==0||i==2||i==4||i==6) {
                row = new Row(i,"White",pieceColor);
            }
            //for rows 1, 3, 5, and 7, the rows will start with black
            else{
                row = new Row(i,"Black",pieceColor);
            }

            //store the row in the gameBoard
            gameBoard[i] = row;
        }
    }

    /**
     * This will check the to see if all of the pieces of
     * one color have been removed from the board
     */
    public boolean colorCleared() {

        // begin with each existence of both colors to false
        boolean redPieceExists = false;
        boolean whitePieceExists = false;

        // for each row and space, check for a piece and its color
        for( Row row: gameBoard){
            for (Space space : row) {
                if (space.getPiece() != null) {
                    if (space.getPiece().getColor() == Color.RED) {
                        redPieceExists = true;
                    }
                    else {
                        whitePieceExists = true;
                    }
                }
            }
        }

        // return if either of the colors doesn't have any pieces
        return !redPieceExists || !whitePieceExists;
    }

    /**
     * This function will update the board with the last move made
     * by deleting the old piece and making a new one in the new position
     *
     * @param move the move made by the piece.
     */
    public void updateBoard(Move move){
        // get position of start and end
        Position start = move.getStart();
        Position end = move.getEnd();

        // split up coordinates for start
        Row startRow = gameBoard[start.getRow()];
        Space startSpace = startRow.row[start.getCell()];

        // split up coordinates for start
        Row endRow = gameBoard[end.getRow()];
        Space endSpace = endRow.row[end.getCell()];

        // get info from piece and delete it
        Piece piece = startSpace.getPiece();
        String type = piece.getType().toString();
        String color = piece.getColor().toString();
        startSpace.deletePiece();

        if (endRow.getIndex() == 0 || endRow.getIndex() == 7) { // Kingify if at opposite end of board.
            type = Piece.Type.KING.toString();
        }

        //create a piece on the new space
        endSpace.createPiece(type,color);

        if (Math.abs(startSpace.getCellIdx() - endSpace.getCellIdx()) == 2) { //if a piece capture jump occured
            Position capturedPos = new Position((startRow.getIndex() + endRow.getIndex())/2, (startSpace.getCellIdx() + endSpace.getCellIdx())/2);
            Row capturedRow = gameBoard[capturedPos.getRow()];
            Space capturedSpace = capturedRow.row[capturedPos.getCell()];
            capturedSpace.deletePiece();
        }
    }

    /**
     * This function will check if a move is a valid move
     * @return true if valid, false if not
     */
    public boolean validMove(Move move){
        Position start = move.getStart();
        Position end = move.getEnd();

        ArrayList<Position> goodMoves = validMoves(move);
        ArrayList<Position> forceMoves = new ArrayList<>();

        //check if must choose piece capture jump.
        for (Position check : goodMoves) {
            if (Math.abs(start.getCell() - check.getCell()) == 2) {
                forceMoves.add(check);
            }
        }

        if (forceMoves.size() != 0) {
            goodMoves = forceMoves;
            moveAllowed = true;
        }

        for(Position check : goodMoves){
            //Position check = goodMoves.get(i);
            if (end.getRow() == check.getRow() && end.getCell() == check.getCell() && moveAllowed) {
                moveAllowed = false;
                return true;
            }
        } return false;
    }

    /**
     * This function will check to see if there is a valid move available
     * on the board for the player based on their last move
     * @param move the move that was made
     * @param possibility the possible move to be checked
     * @param direction the direction based on the move to check
     */
    private Position validMovesHelper(Move move, Position possibility, String direction){

        // get the values for the move move
        Position start = move.getStart();
        Row startRow = gameBoard[start.getRow()];
        Space startSpace = startRow.row[start.getCell()];

        // get the values for the possible move
        Row possibilityRow = gameBoard[possibility.getRow()];
        Space possibilitySpace = possibilityRow.row[possibility.getCell()];

        // if the possible space has a piece
        if (possibilitySpace.getPiece() != null) {
            // if the color of the pieces are different
            if (possibilitySpace.getPiece().getColor() != startSpace.getPiece().getColor()) {
                Position possibility3 = null;
                switch (direction) {
                    // check if the jump is possible for up right space
                    case "UR":
                        if (possibility.getCell() < 7 && possibility.getRow() >= 1) {
                            possibility3 = new Position(possibility.getRow() - 1, possibility.getCell() + 1);
                        }
                        break;
                    // check if the jump is possible for up left space
                    case "UL":
                        if (possibility.getCell() >= 1 && possibility.getRow() >= 1) {
                            possibility3 = new Position(possibility.getRow() - 1, possibility.getCell() - 1);
                        }
                        break;
                    // check if the jump is possible for down left space
                    case "DL":
                        if (possibility.getCell() >= 1 && possibility.getRow() < 7) {
                            possibility3 = new Position(possibility.getRow() + 1, possibility.getCell() - 1);
                        }
                        break;
                    // check if the jump is possible for down right space
                    case "DR":
                        if (possibility.getCell() < 7 && possibility.getRow() < 7) {
                            possibility3 = new Position(possibility.getRow() + 1, possibility.getCell() + 1);
                        }
                        break;
                }
                // if none of the jumps are valid
                if (possibility3 == null) {
                    return null;
                }

                //get the values for the possible jump
                Row possibility3Row = gameBoard[possibility3.getRow()];
                Space possibility3Space = possibility3Row.row[possibility3.getCell()];

                // checks if a piece is in the possible space
                if (possibility3Space.getPiece() == null) { return possibility3; }
                else { return null; }
            }
            else { return null; }
        } else{
            if (!moveAllowed) { return null; }
            else { return possibility; }
        }
    }

    /**
     * This function will check if the starting position of
     * a move made has any valid spaces and return those that are valid
     * @param move the move that was made
     * @return the arraylist of possible moves
     */
    private ArrayList<Position> validMoves(Move move) {

        // get the start position of the move
        ArrayList<Position> goodMoves = new ArrayList<>();
        Position start = move.getStart();
        Row startRow = gameBoard[start.getRow()];
        Space startSpace = startRow.row[start.getCell()];

        // get info on piece that moved
        Piece piece = startSpace.getPiece();

        // for a normal piece
        if (piece.getType() == Piece.Type.SINGLE){ //check spaces in front
            Position possibility1 = new Position(start.getRow()-1, start.getCell()+1);
            Position possibility2 = new Position(start.getRow()-1, start.getCell()-1);

            // check if space up right is available
            if(possibility1.getCell()<8) {
                Position goodMove = validMovesHelper(move, possibility1, "UR");
                if(goodMove != null) { //if move valid
                    goodMoves.add(goodMove);
                }
            }
            // check if space up left is available
            if(possibility2.getCell()>=0) {
                Position goodMove = validMovesHelper(move, possibility2, "UL");
                if(goodMove != null) {
                    goodMoves.add(goodMove);
                }
            }
        }

        // for a king piece
        if (piece.getType() == Piece.Type.KING){ //check spaces around
            Position possibility1 = new Position(start.getRow()-1, start.getCell()+1);
            Position possibility2 = new Position(start.getRow()-1, start.getCell()-1);
            Position possibility3 = new Position(start.getRow()+1, start.getCell()-1);
            Position possibility4 = new Position(start.getRow()+1, start.getCell()+1);

            // check if space up right is available
            if(possibility1.getCell() < 8 && possibility1.getRow() >= 0) {
                Position goodMove = validMovesHelper(move, possibility1, "UR");
                if(goodMove != null) {
                    goodMoves.add(goodMove);
                }
            }
            // check if space up left is available
            if(possibility2.getCell() >= 0 && possibility2.getRow() >= 0) {
                Position goodMove = validMovesHelper(move, possibility2, "UL");
                if(goodMove != null) {
                    goodMoves.add(goodMove);
                }
            }

            // check if space down left is available
            if(possibility3.getCell() >= 0 && possibility3.getRow() < 8) {
                Position goodMove = validMovesHelper(move, possibility3, "DL");
                if(goodMove != null) {
                    goodMoves.add(goodMove);
                }
            }

            // check if space down right is available
            if(possibility4.getCell() < 8 && possibility4.getRow() < 8) {
                Position goodMove = validMovesHelper(move, possibility4, "DR");
                if(goodMove != null) {
                    goodMoves.add(goodMove);
                }
            }
        }
        // return the moves that are possible
        return goodMoves;
    }

    /**
     * This function will set if a move is allowed to happen
     * @param moveAllowed the boolean for the move allowance
     */
    public void setMoveAllowed(boolean moveAllowed) {
        this.moveAllowed = moveAllowed;
    }

    @Override
    public Iterator<Row> iterator() {
        return new Iterator<>() {

            private int currentRow = 0;

            /**
             * Returns true if the iteration has more elements.
             * (In other words, returns true if next() would
             * return an element rather than throwing an exception.)
             *
             * @return {@code true} if the iteration has more elements
             */
            @Override
            public boolean hasNext() {
                return currentRow < 8;
            }

            /**
             * Returns the next element in the iteration.
             *
             * @return the next element in the iteration
             * @throws NoSuchElementException if the iteration has no more elements
             */
            @Override
            public Row next() {
                if(this.hasNext()) {
                    int current = currentRow;
                    currentRow ++;
                    return gameBoard[current];
                }
                throw new NoSuchElementException();
            }
        };

    }
}
