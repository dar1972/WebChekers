package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.util.Message;
import spark.*;

import java.util.Objects;
import java.util.logging.Logger;

//Made, worked on, commented, and cleaned by Beck

public class PostCheckTurnRoute implements Route{


    private static final Logger LOG = Logger.getLogger(PostSignInRoute.class.getName());

    private final Gson gson;
    private final GameCenter gameCenter;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP
     * requests.
     *
     */
    public PostCheckTurnRoute(GameCenter gameCenter,Gson gson) {
        Objects.requireNonNull( gameCenter );
        this.gameCenter = gameCenter;
        this.gson = gson;
        LOG.config("PostCheckTurnRoute is initialized.");
    }

    /**
     * This function will render the WebCheckers Game page after a turn check.
     * @param request the HTTP request
     * @param response the HTTP response
     * @return the json
     */
    @Override
    public Object handle(Request request, Response response) {

        LOG.finer("PostCheckTurnRoute is invoked.");
        String userName = request.session().attribute("userName");
        Message message;

        // does player exist?
        if (userName != null) {
            // if player is active
            if (gameCenter.getGame(userName).getActivePlayer().getName().equals(userName)) {
                message = Message.info("true");
            }
            else {
                message = Message.info("false");
            }
            // if player is the winner
            if (gameCenter.getGame(userName).isWinner()){
                message = Message.info("true");
            }
        }
        else {
            message = Message.info("true"); //Not actually true, but triggers page refresh
                                            // when this route is invoked after server restart.
        }
        // get ready to send message
        String json;
        json = gson.toJson(message);
        return json;
    }
}
