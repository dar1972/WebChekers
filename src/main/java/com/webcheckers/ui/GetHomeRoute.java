package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;
import static spark.Spark.halt;


import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:mok8867@rit.edu'>Marcus Kapoor</a>
 */
public class GetHomeRoute implements Route {

  // File created by Marcus, commented and cleaned by Beck

  private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

  private static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");
  private static final Message SELECT_PLAYER_MSG = Message.info("Select a player to enter a game with.");

  private static final Message PLAYER_BUSY_MSG = Message.error("The player you selected was busy. Please try another one.");
  private static final Message NOT_SELECTED_MSG = Message.error("You didn't select a player or the page refreshed right before you confirmed your selection. Please try again.");

  static final String USER_PARAM = "userName";
  static final String USER_BUSY = "userBusy";
  static final String NOT_SELECTED = "notSelected";


  static final String VIEW_NAME = "home.ftl";
  static final String TITLE_ATTR = "title";
  static final String TITLE = "Welcome!";


  private final TemplateEngine templateEngine;
  private final PlayerLobby playerLobby;
  private final GameCenter gameCenter;
  /**
   * This function creates the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
   *
   * @param templateEngine the HTML template rendering engine
   */
  public GetHomeRoute(final TemplateEngine templateEngine, final PlayerLobby playerLobby, final GameCenter gameCenter) {
    this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    this.playerLobby = playerLobby;
    this.gameCenter = gameCenter;
    LOG.config("GetHomeRoute is initialized.");
  }

  /**
   * This function will render the WebCheckers Home page.
   *
   * @param request the HTTP request
   * @param response the HTTP response
   * @return the rendered HTML for the Home page
   */
  @Override
  public Object handle(Request request, Response response) {
    LOG.finer("GetHomeRoute is invoked.");

    // set up HashMap to add string values to the variables
    Map<String, Object> vm = new HashMap<>();
    vm.put(TITLE_ATTR, TITLE);

    // get username from players and put them in the player lobby
    String userName = request.session().attribute(USER_PARAM);
    Player player = playerLobby.getPlayers().get(userName);

    // display a user message in the Home page
    if (request.session().attribute(USER_BUSY) == "yes") {
      vm.put("message", PLAYER_BUSY_MSG);
    }
    // no player was selected
    else if (request.session().attribute(NOT_SELECTED) == "yes") {
      vm.put("message", NOT_SELECTED_MSG);
    }
    // player needs to put in a username
    else if (userName != null) {
      vm.put("message", SELECT_PLAYER_MSG);
    }
    // player in the lobby
    else {
      vm.put("message", WELCOME_MSG);
    }
    // makes it so that player does not see their own name in a list of potential opponents
    HashMap<String, Player> processedHashMap = new HashMap<> (playerLobby.getPlayers());
    processedHashMap.remove(userName);
    HashMap<Integer, Game> archivedGames = gameCenter.getArchivedGames();

      // if username is valid
      if (userName != null) {
        vm.put("currentUser", player);
        vm.put("userList",processedHashMap);
      }

      vm.put("lobbySize", playerLobby.getPlayers().size());
      vm.put("archivedGames", archivedGames);
      vm.put("archiveSize", archivedGames.size());

      // once entering opponents name, both players will be sent to the game if both are available
      if (gameCenter.getGameLobby().containsKey(userName)) {
        if (gameCenter.getGame(userName).getWinner() == player) {
            gameCenter.leaveGame(player);
        } else {
            response.redirect("/game");
            halt();
        }
      }
    // render the View
    return templateEngine.render(new ModelAndView(vm , VIEW_NAME));
  }
}
