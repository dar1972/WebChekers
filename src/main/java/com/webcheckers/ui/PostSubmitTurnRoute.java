package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.BoardView;
import com.webcheckers.model.Game;
import com.webcheckers.util.Message;
import spark.*;

import java.util.ArrayList;
import java.util.logging.Logger;

//Made by Beck, worked on by Marcus, commented, and cleaned by Beck

public class PostSubmitTurnRoute implements Route{
    private static final Logger LOG = Logger.getLogger(PostSignInRoute.class.getName());

    static final String USER_PARAM = "userName";
    private static final String SWAP_TURN_ERROR = "Could not submit turn, please try again!";
    private static final String SUBMIT_TURN_INFO = "Turn submitted!";
    private static final String GAME_OVER_INFO = "The game is over. Please head back to home!";

    private final GameCenter gameCenter;
    private final Gson gson;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP
     * requests.
     *
     */
    public PostSubmitTurnRoute(GameCenter gameCenter,Gson gson) {
        this.gameCenter = gameCenter;
        this.gson = gson;
        LOG.config("PostSubmitTurnRoute is initialized.");
    }

    /**
     * This function will render the WebCheckers Game page
     * after a turn is submitted
     * @param request the HTTP request
     * @param response the HTTP response
     * @return json
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.config("PostSubmitTurnRoute is invoked.");

        Message message;
        String userName = request.session().attribute( USER_PARAM );

        // if player is in the game
        if(gameCenter.isPlayerInGame(userName)){
            boolean outcome = gameCenter.finishTurn(userName);

            // if turn not over
            if(!outcome){
                message = Message.error("Fix something, this ain't right!");
            }
            // if active player is username
            else if(gameCenter.isPlayerActive(userName)){
                Game game = gameCenter.getGame(userName);
                message = Message.info(SUBMIT_TURN_INFO);

                // get the move snaps
                ArrayList<BoardView> activeSnapshots = game.getActiveSnapshots();
                ArrayList<BoardView> inactiveSnapshots = game.getInactiveSnapshots();
                ArrayList<BoardView> tempSnapshots = game.getTempSnapshots();

                BoardView activeBoard = activeSnapshots.get(activeSnapshots.size()-1);

                // for each snap in tempSnapshots
                for (BoardView tempSnapshot : tempSnapshots) {
                    inactiveSnapshots.add(game.copyBoard(tempSnapshot));
                }

                // remove a move from opponents board
                BoardView inactiveBoard = inactiveSnapshots.get(inactiveSnapshots.size()-1);

                // set the boards
                game.setGameBoardRed(activeBoard);
                game.setGameBoardWhite(inactiveBoard);

                // flip moves allowed
                game.resetMoveAllowed();

                tempSnapshots.clear();
                //check if a color has been fully wiped from the board.
                if (game.colorCleared()) {
                    game.setWinner(game.getActivePlayer()); 
                }
                game.updateActivePlayer();
            } else{
                message = Message.info(SWAP_TURN_ERROR);
            }
        } else {
            message = Message.info(GAME_OVER_INFO);
        }
        // get ready to send message
        String json;
        json = gson.toJson(message);
        return json;
    }
}

