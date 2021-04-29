package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Player Test class tests the player model.
 */

@Tag("Model-tier")
public class PlayerTest {

    //private attributes with usernames
    private static final String userName1 = "Dhruv";
    private static final String userName2 = "Marcus";
    private static final String userName3 = "Beck";
    private static final String userName4 = "Kelly";


    @Test
    public void getName(){
        final Player player = new Player(userName1);
        assertEquals(userName1, player.getName(), "Name not equal to " + userName1);
        assertEquals(userName2, player.getName(), "Name not equal to " + userName2);
        assertEquals(userName3, player.getName(), "Name not equal to " + userName3);
        assertEquals(userName4, player.getName(), "Name not equal to " + userName4);
    }


    @Test
    public void equals(){
        final Player player1 = new Player(userName1);
        final Player player2 = new Player(userName1);
        final Player player3 = new Player(userName2);
        final Player player4 = new Player(userName3);
        final Player player5 = new Player(userName4);

        assertTrue(player1.equals(player1));
        assertTrue(player1.equals(player2));
        assertFalse(player1.equals(player3));
        assertFalse(player4.equals(player5));
    }


    @Test
    public void test_hashCode(){
        final Player player1 = new Player(userName1);
        final Player player2 = new Player(userName2);
        final Player player3 = new Player(userName3);
        final Player player4 = new Player(userName4);
        assertEquals(userName1.hashCode(), player1.hashCode());
        assertEquals(userName2.hashCode(), player2.hashCode());
        assertEquals(userName3.hashCode(), player3.hashCode());
        assertEquals(userName4.hashCode(), player4.hashCode());
    }
}