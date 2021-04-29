package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;

import spark.*;

//Created by Kelly, commented, and cleaned by Beck
public class PostResignGameRoute implements Route {

    private final GameCenter gameCenter;
    private final PlayerLobby playerLobby;
    private final Gson gson;

    /**
     * This function create the Spark Route (UI controller) to
     * handle all {@code GET /} HTTP requests.
     */
    public PostResignGameRoute(GameCenter gameCenter, PlayerLobby playerLobby, Gson gson) {
        this.gameCenter = gameCenter;
        this.playerLobby = playerLobby;
        this.gson = gson;
    }

    /**
     * This function will render the WebCheckers Game page for
     * after a resignation.
     * @param request the HTTP request
     * @param response the HTTP response
     * @return json
     */
    @Override
    public Object handle(Request request, Response response) {
        
        String userName = request.session().attribute("userName");
        Message msg;
        String json;

        // if opponent already resigned
        if (!gameCenter.getGame(userName).getPlayerWhoResigned().equals("")) {
            msg = Message.info("Opponent already resigned!");
            json = gson.toJson(msg);
            return json;
        }
        // get the player who resigned and remove them
        gameCenter.getGame(userName).setPlayerWhoResigned(userName);
        Player opponent = gameCenter.getOpponent(playerLobby.getPlayers().get(userName));
        gameCenter.leaveGame(playerLobby.getPlayers().get(userName));

        // if person doesn't exist
        if(gameCenter.getGame(opponent.getName()).getPlayerWhoResigned().equals("")) {
            msg = Message.error("Resign failed.");
        }
        else {
            msg = Message.info("Resign success");
        }
        // get ready to send message
        json = gson.toJson(msg);
        return json;
    }
}
