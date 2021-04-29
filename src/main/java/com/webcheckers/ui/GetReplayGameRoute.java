package com.webcheckers.ui;

//Created by Dhruv, commented and cleaned by Beck
import com.google.gson.Gson;
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

public class GetReplayGameRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetReplayGameRoute.class.getName());

    static final String TITLE = "Game Spectate";
    private final GameCenter gameCenter;
    private final PlayerLobby playerLobby;
    private final TemplateEngine templateEngine;
    private final Gson gson;

    private enum ViewMode {
        REPLAY
    }

    /**
     * This function creates the Spark Route (UI controller) to handle
     * all {@code GET /} HTTP requests.
     * @param templateEngine the HTML template rendering engine
     */
    public GetReplayGameRoute(GameCenter gameCenter, TemplateEngine templateEngine,
                                        Gson gson, PlayerLobby playerLobby) {
        this.gameCenter = gameCenter;
        this.playerLobby = playerLobby;
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        this.gson = gson;
        LOG.config("GetReplayGameRoute is initialized.");
    }

    /**
     * This function will render the WebCheckers Game Replay page.
     * @param request the HTTP request
     * @param response the HTTP response
     * @return the rendered HTML for the Game Replay page
     */
    @Override
    public Object handle(Request request, Response response) {

        Session httpSession = request.session();
        LOG.finer("GetReplayGameRoute is invoked.");
        Map<String, Object> vm = new HashMap<>();
        vm.put(GetHomeRoute.TITLE_ATTR, TITLE);
        
        if (httpSession.attribute("userName") == null) {
            response.redirect(WebServer.HOME_URL);
            halt();
        }
        // add player to the replay lobby
        Player currentUser = playerLobby.getPlayers().get(httpSession.attribute("userName"));

        // set player to spectator
        currentUser.setSpectating(true);
        // get the archived game
        Game game = gameCenter.getArchivedGames().get(httpSession.attribute("viewedGameID"));
        int replayIndex;
        // if replayIndex doesn't exist
        if (httpSession.attribute("replayIndex") == null) {
            replayIndex = 0;
            httpSession.attribute("replayIndex", 0);
        }
        else {
            replayIndex = httpSession.attribute("replayIndex");
        }

        // set up page
        vm.put("title", "Game Time!");
        vm.put("currentUser", currentUser);
        vm.put("redPlayer", game.getRed());
        vm.put("whitePlayer", game.getWhite());
        vm.put("activeColor", game.getActiveColor());
        vm.put("viewMode", ViewMode.REPLAY);
        vm.put("board", game.getRedSnapshots().get(replayIndex));

        final Map<String, Object> modeOptions = new HashMap<>();

        // if there are previous moves
        if (replayIndex <= 0) {
            modeOptions.put("hasPrevious", false);
        }
        else {
            modeOptions.put("hasPrevious", true);
        }
        // if there are more moves
        if (replayIndex >= game.getRedSnapshots().size()-1) {
            modeOptions.put("hasNext", false);
        }
        else {
            modeOptions.put("hasNext", true);
        }
        vm.put("modeOptionsAsJSON", gson.toJson(modeOptions));  

        // return the page setup
        return templateEngine.render(new ModelAndView(vm , "game.ftl"));
    }
}