package com.webcheckers.ui;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.Game;
import spark.*;

import java.util.Objects;
import java.util.logging.Logger;
import static spark.Spark.halt;

//created by Dhruv, commented and cleaned by Beck
public class GetSpectateReplayChooserRoute implements Route {

    private static final Logger LOG = Logger.getLogger(GetSpectateReplayChooserRoute.class.getName());

    static final String TITLE = "Redirecting...";   // used in testing
    private final GameCenter gameCenter;

    /**
     * This function creates the Spark Route (UI controller) to handle
     * all {@code GET /} HTTP requests.
     * @param templateEngine the HTML template rendering engine
     */
    public GetSpectateReplayChooserRoute(GameCenter gameCenter, TemplateEngine templateEngine) {
        Objects.requireNonNull(templateEngine, "templateEngine is required");
        this.gameCenter = gameCenter;
        LOG.config("GetSpectateReplayChooser is initialized.");
    }

    /**
     * This function will render the WebCheckers Replay Chooser page.
     * @param request the HTTP request
     * @param response the HTTP response
     * @return null
     */
    @Override
    public Object handle(Request request, Response response) {

        LOG.finer("GetSpectateReplayChooser is invoked.");
        int gameID= Integer.parseInt(request.queryParams("viewedGameID"));
        request.session().attribute( "viewedGameID", gameID );

        request.session().attribute("spectateIndex", null);
        request.session().attribute("replayIndex", null);

        // get the game from the ID
        Game game = gameCenter.getGame(gameID);
        // is game over?
        if (game.getIsOver()) {
            response.redirect("/replay/game");
        }
        else {
            response.redirect("/spectator/game");
        }
        halt();
        return null;
    }

}
