package com.webcheckers.model;

// Created by Dhruv, commented, and cleaned by Beck
public class Move {

    private final Position start;
    private final Position end;

    /**
     * This will create a new instance of a move
     * @param startPos The starting position
     * @param endPos The ending position
     */
    public Move(Position startPos, Position endPos) {
        this.start = startPos;
        this.end = endPos;
    }

    /**
     * This function will get the starting position
     * @return The starting position
     */
    public Position getStart() {
        return start;
    }

    /**
     * This function will get the ending position
     * @return The ending position
     */
    public Position getEnd() {
        return end;
    }

    /**
     * This function will invert the move for storage
     * on the opponent's board
     * @return the inverted move
     */
    public Move invertMove(){

        // Get the inverted position of the move
        Position start = new Position(7-this.start.getRow(), 7-this.start.getCell());
        Position end = new Position(7-this.end.getRow(), 7-this.end.getCell());

        return new Move(start, end);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Move)){
            return false;
        }
        else {
            Move that = (Move)o;
            return that.start.equals(this.start) && that.end.equals(this.end);
        }
    }
}
