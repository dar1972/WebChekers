package com.webcheckers.ui;

//created by Dhruv, commented and cleaned by Beck

import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;

import spark.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import static spark.Spark.halt;

public class GetSpectatorGame implements Route {
    private static final Logger LOG = Logger.getLogger(GetSpectatorGame.class.getName());

    static final String TITLE = "Game Spectate";
    private final GameCenter gameCenter;
    private final PlayerLobby playerLobby;
    private final TemplateEngine templateEngine;


    private enum ViewMode{
        SPECTATOR
    }

    /**
     * This function creates the Spark Route (UI controller) to handle
     * all {@code GET /} HTTP requests.
     * @param templateEngine the HTML template rendering engine
     */
    public GetSpectatorGame(GameCenter gameCenter, TemplateEngine templateEngine, PlayerLobby playerLobby) {
        this.gameCenter = gameCenter;
        this.playerLobby = playerLobby;
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        LOG.config("GetSpectateGameRoute is initialized.");
    }

    /**
     * This function will render the WebCheckers Replay page.
     * @param request the HTTP request
     * @param response the HTTP response
     * @return the rendered HTML for the Replay page
     */
    @Override
    public Object handle(Request request, Response response) {
        final Session httpSession = request.session();
        LOG.finer("GetSpectateGameRoute is invoked.");
        Map<String, Object> vm = new HashMap<>();
        vm.put(GetHomeRoute.TITLE_ATTR, TITLE);

        // if the username is invalid
        if (httpSession.attribute("userName") == null) {
            response.redirect(WebServer.HOME_URL);
            halt();
        }

        // get the user and game
        Player currentUser = playerLobby.getPlayers().get(httpSession.attribute("userName"));

        currentUser.setSpectating(true);
        Game game = gameCenter.getArchivedGames().get(httpSession.attribute("viewedGameID"));

        vm.put("title", "Game Time!");
        vm.put("currentUser", currentUser);
        vm.put("redPlayer", game.getRed());
        vm.put("whitePlayer", game.getWhite());
        vm.put("activeColor", game.getActiveColor());
        vm.put("viewMode", ViewMode.SPECTATOR);
        vm.put("board", game.getRedSnapshots().get(game.getRedSnapshots().size()-1));

        // checks the spectateIndex for size
        if (httpSession.attribute("spectateIndex") == null) {
            httpSession.attribute("spectateIndex", 0);
        } else {
            httpSession.attribute("spectateIndex");
        }
        return templateEngine.render(new ModelAndView(vm , "game.ftl"));
    }
}