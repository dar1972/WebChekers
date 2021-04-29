package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.Game;
import com.webcheckers.util.Message;
import spark.*;

import java.util.logging.Logger;

//Created By Dhruv, commented, and cleaned by Beck
public class PostSpectatorCheckTurnRoute implements Route {

    private static final Logger LOG = Logger.getLogger(PostCheckTurnRoute.class.getName());
    private final GameCenter gameCenter;
    private final Gson gson;

    /**
     * This function create the Spark Route (UI controller) to
     * handle all {@code GET /} HTTP requests.
     */
    public PostSpectatorCheckTurnRoute(GameCenter gameCenter, Gson gson){
        LOG.config("PostSpectateSpectateCheckTurnRoute is initialized.");
        this.gameCenter = gameCenter;
        this.gson = gson;
    }

    /**
     * This function will render the WebCheckers Game page for
     * a spectator
     * @param request the HTTP request
     * @param response the HTTP response
     * @return json
     */
    @Override
    public Object handle(Request request, Response response) {

        LOG.finer("PostSpectateCheckTurnRoute is invoked.");
        final Session httpSession = request.session();

        String userName = httpSession.attribute("userName");
        Game game = gameCenter.getArchivedGames().get(httpSession.attribute("viewedGameID"));

        Message message;
        // if player exists
        if (userName != null) {
            int spectateIndex;
            // if spectators are empty
            if (httpSession.attribute("spectateIndex") == null) {
                spectateIndex = 0;
                httpSession.attribute("spectateIndex", 0);
            }
            else {
                spectateIndex = httpSession.attribute("spectateIndex");
            }
            // get the game for the spectator if it exists
            if (game.getInactiveSnapshots().size()-1 > spectateIndex) {
                message = Message.info("true");
                spectateIndex = game.getInactiveSnapshots().size()-1;
                httpSession.attribute("spectateIndex", spectateIndex);
            }
            else {
                message = Message.info("false");
            }
        }
        else {
            message = Message.info("true"); //not actually, but triggers page refresh to exit.
        }
        // get ready to send message
        String json;
        json = gson.toJson(message);
        return json;
    }
}
