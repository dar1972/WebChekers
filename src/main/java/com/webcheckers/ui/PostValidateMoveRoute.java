package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.BoardView;
import com.webcheckers.model.Game;
import com.webcheckers.model.Move;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.ArrayList;
import java.util.logging.Logger;

// Created by Beck, edited by Dhruv, commented, and cleaned by Beck

public class PostValidateMoveRoute implements Route {

    private static final Logger LOG = Logger.getLogger(GetGameRoute.class.getName());
    private final GameCenter gameCenter;
    private final Gson gson;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP
     * requests.
     */
    public PostValidateMoveRoute(GameCenter gameCenter, Gson gson) {
        this.gameCenter = gameCenter;
        this.gson = gson;
        LOG.config("PostValidateRoute is initialized.");
    }

    /**
     * This function will render the WebCheckers Game page
     * after a turn is validated
     * @param request the HTTP request
     * @param response the HTTP response
     * @return json
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("PostValidateRoute is invoked");
        String actionData = request.queryParams("actionData");
        String userName = request.session().attribute("userName");

        // set up move, game, and activeSnapshots
        Move move = gson.fromJson(actionData, Move.class);
        Game game = gameCenter.getGame(userName);
        ArrayList<BoardView> activeSnapshots = gameCenter.getGame(userName).getActiveSnapshots();

        Message msg;
        // if move exists and is valid
        if(activeSnapshots.get(activeSnapshots.size()-1).validMove(move)){
            msg = Message.info("Valid Move");
            gameCenter.storeMove(move, game);
        } else{
            msg = Message.error("invalid move, please try another.");
        }
        // get ready to send message
        String json;
        json = gson.toJson(msg);
        return json;
    }
}