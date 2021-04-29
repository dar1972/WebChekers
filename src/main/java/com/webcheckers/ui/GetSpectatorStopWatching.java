package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import static spark.Spark.halt;

//created by Dhruv, commented and cleaned by Beck

public class GetSpectatorStopWatching implements Route {

    private static final Logger LOG = Logger.getLogger(GetSpectatorStopWatching.class.getName());

    static final String TITLE = "Spectate Exit";
    private final PlayerLobby playerLobby;

    /**
     * This function creates the Spark Route (UI controller) to handle
     * all {@code GET /} HTTP requests.
     * @param templateEngine the HTML template rendering engine
     */
    public GetSpectatorStopWatching(TemplateEngine templateEngine, PlayerLobby playerLobby) {
        Objects.requireNonNull(templateEngine, "templateEngine is required");
        this.playerLobby = playerLobby;
        LOG.config("GetSpectatorStopWatching is initialized.");
    }

    /**
     * This function will render the WebCheckers Home page (done spectating).
     * @param request the HTTP request
     * @param response the HTTP response
     * @return null
     */
    @Override
    public Object handle(Request request, Response response) {

        final Session httpSession = request.session();
        LOG.finer("GetSpectateStopWatching is invoked.");
        Map<String, Object> vm = new HashMap<>();
        vm.put(GetHomeRoute.TITLE_ATTR, TITLE);

        String userName = httpSession.attribute("userName");

        // does the spectator exist?
        if (userName != null) {
            Player user = playerLobby.getPlayers().get(userName);
            user.setSpectating(false);
            vm.put("currentUser", user);
        }

        // render page
        response.redirect(WebServer.HOME_URL);
        halt();
        return null;
    }

}
