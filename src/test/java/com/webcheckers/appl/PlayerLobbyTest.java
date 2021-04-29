package com.webcheckers.appl;

import static org.junit.jupiter.api.Assertions.*;

import com.webcheckers.model.Player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit test suite for the {@link PlayerLobby} component.
 *
 * @author <a href='mailto:mok8867@rit.edu'>Marcus Kapoor</a>
 */
@Tag("Application-tier")
public class PlayerLobbyTest {

    private PlayerLobby testLobby;

  @BeforeEach
  public void testCreateLobby() {
    testLobby = new PlayerLobby();
    assertNotNull(testLobby);
  }

  /**
   * Test that you can input into lobby.
   */
  @Test
  public void test_add_players() {
    testLobby.addToLobby(new Player("Bobby"));
    testLobby.addToLobby(new Player("Tables"));

    assertEquals(2, testLobby.getPlayers().size());
  }

    /**
   * Test that you can retrieve accurate data from lobby.
   */
  @Test
  public void test_get_players() {
    testLobby.addToLobby(new Player("Bobby"));
    testLobby.addToLobby(new Player("Tables"));
    assertEquals(true, testLobby.getPlayers().containsKey("Bobby"));
    assertEquals(true, testLobby.getPlayers().containsKey("Tables"));
    assertEquals(false, testLobby.getPlayers().containsKey("Imposter"));

  }

}
