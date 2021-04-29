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

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;

import static spark.Spark.halt;


public class PostSignInRoute implements Route {
  private static final Logger LOG = Logger.getLogger(PostSignInRoute.class.getName());

  private static final Message SIGNIN_INFO_MSG = Message.info("Please log in using an existing username or a new one.");
  private static final Message NAME_TAKEN_MSG = Message.error("Someone is already logged in with that username. Please try a different one");
  private static final Message INVALID_NAME_MSG = Message.error("The username you entered is not valid. Please try a different one.");



  static final String USER_PARAM = "userName";

  static final String USER_IN_ATTR = "currentUser??"; //used by nav-bar.ftl to decide if to display username or not.
  static final String USER_NAME_ATTR = "currentUser.name"; //use by nav-bar.ftl to display the username.

  private final TemplateEngine templateEngine;
  private final PlayerLobby playerLobby;

  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP
   * requests.
   *
   * @param templateEngine the HTML template rendering engine
   */
  public PostSignInRoute(final TemplateEngine templateEngine, final PlayerLobby playerLobby) {
    this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby is required");
    //
    LOG.config("PostSignInRoute is initialized.");
  }

  /**
   * Render the WebCheckers Home page.
   *
   * @param request
   *   the HTTP request
   * @param response
   *   the HTTP response
   *
   * @return
   *   the rendered HTML for the Home page
   */
  @Override
  public Object handle(Request request, Response response) {
    LOG.finer("PostSignInRoute is invoked.");

    // used to put in title name, this varies depending on which page it is
    Map<String, Object> vm = new HashMap<>();
    vm.put("title", "Sign In!");

    // display a user message in the Home page
    vm.put("message", SIGNIN_INFO_MSG);

    final String userName = request.queryParams(USER_PARAM);

    // if user gives invalid username
    if (!playerLobby.isValid(userName)) {
      vm.put("message", INVALID_NAME_MSG);
      return templateEngine.render(new ModelAndView(vm , "signin.ftl"));
    }

    Player player = new Player(userName);

    //so pass username to playerlobby?
    boolean success = playerLobby.addToLobby(player);

    if (success) { // if player is added to lobby
      request.session().attribute( USER_PARAM, userName );
      response.redirect(WebServer.HOME_URL);
      halt();
      //return null;
      return templateEngine.render(new ModelAndView(vm, "game.ftl")); // created by Marcus, adjusted by Kelly
    }
    else {
      vm.put("message", NAME_TAKEN_MSG); // example of invalid name: if it is already taken

      // render the View
      return templateEngine.render(new ModelAndView(vm , "signin.ftl"));
    }


  }
}
