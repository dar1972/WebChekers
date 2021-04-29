package com.webcheckers.model;

import java.io.Serializable;

public class Piece implements Serializable {
    //Created, commented, and cleaned by Beck

    //Types of pieces
    enum Type{
        SINGLE,
        KING}

    //Colors of pieces
    public enum Color{
        RED,
        WHITE}

    private final Type type;
    private final Color color;

    /**
     * Constructor for a new piece
     * @param type the type of piece (KING/SINGLE)
     * @param color the color of the piece (RED/WHITE)
     */
    public Piece(String type, String color){
        if(type.equals("s")||type.equals("SINGLE")){
            this.type = Type.SINGLE;
        }else{
            this.type = Type.KING;
        }if(color.equals("r")||color.equals("RED")){
            this.color = Color.RED;
        }else{
            this.color = Color.WHITE;
        }
    }

    /**
     * Gets the type piece is (KING/SINGLE)
     * @return the type
     */
    public Type getType(){
        if(type.equals(Type.SINGLE)){
            return Type.SINGLE;
        }
        else {
            return Type.KING;
        }
    }

    /**
     * Gets the color of the piece (RED/WHITE)
     * @return the color
     */
    public Color getColor(){
        if(color.equals(Color.RED)){
            return Color.RED;
        }
        else {
            return Color.WHITE;
        }
    }
}
