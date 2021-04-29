package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.Game;
import com.webcheckers.util.Message;

import spark.*;

public class PostBackupMoveRoute implements Route {

    private final GameCenter gameCenter;
    private final Gson gson;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP
     * requests.
     */
    public PostBackupMoveRoute(GameCenter gameCenter, Gson gson) {
        this.gameCenter = gameCenter;
        this.gson = gson;
    }

    /**
     * This function will render the WebCheckers Game page after a back up.
     * @param request the HTTP request
     * @param response the HTTP response
     * @return the json
     */
    @Override
    public Object handle(Request request, Response response) {
        // set up game and user
        String userName = request.session().attribute("userName");
        Game game = gameCenter.getGame(userName);
        // is back up possible
        boolean success = game.backupMove();
        Message msg;
        if (success) {
            msg = Message.info("Backup success.");
        }
        else {
            msg = Message.error("Backup failed.");
        }
        // get ready to send message
        String json;
        json = gson.toJson(msg);

        return json;
    }
}
