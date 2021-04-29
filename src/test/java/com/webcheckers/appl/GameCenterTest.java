package com.webcheckers.appl;

import static org.junit.jupiter.api.Assertions.*;

import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * GameCenter Test class tests the GameCenter application.
 */

public class GameCenterTest {

    private final PlayerLobby testLobby = new PlayerLobby();

    /**
     * Test the ability to make a new PlayerLobby.
     */
    public void test_make_player_service() {
        final GameCenter CuT = new GameCenter(testLobby);
        // Invoke test
        final Map<String, Game> playerSvc = CuT.getGameLobby();
        // Analyze results
        assertNotNull(playerSvc);
    }

    /**
     * Test the ability to make a new game.
     */
    @Test
    public void test_make_game() {
        final GameCenter CuT = new GameCenter(testLobby);
        String testRed = "Paul";
        String testWhite = "Blart";
        CuT.createGame(testRed, testWhite);
        final Player testRedPlayer = new Player("Paul");
        final Player testWhitePlayer = new Player("Blart");
        // Invoke test
        final Map<String, Game> game = CuT.getGameLobby();

        // Analyze the results
        // 1) the returned game is real
        assertNotNull(game);
        // 2) Paul is the opponent
        assertEquals(testWhitePlayer, CuT.getOpponent(testRedPlayer));
        // 3) Blart is the opponent
        assertEquals(testRedPlayer, CuT.getOpponent(testWhitePlayer));
    }


}
