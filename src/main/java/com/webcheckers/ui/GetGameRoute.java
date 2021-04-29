package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.BoardView;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import static spark.Spark.halt;


public class GetGameRoute implements Route {

    // File created by Beck Anderson, code by Marcus, code adjusted by Kelly, commented and cleaned by Beck

    private static final Logger LOG = Logger.getLogger(GetGameRoute.class.getName());
    static final String USER_PARAM = "userName";
    static final String Game_ID = "gameID";   // used in other classes


    private final TemplateEngine templateEngine;
    private final PlayerLobby playerLobby;
    private final GameCenter gameCenter;

    private final Gson gson;

    private enum ViewMode{
        PLAY
    }


    /**
     * This function creates the Spark Route (UI controller) to handle
     * all {@code GET /} HTTP requests.
     *
     * @param templateEngine the HTML template rendering engine
     */
    public GetGameRoute(final TemplateEngine templateEngine, final PlayerLobby playerLobby, final GameCenter gameCenter, Gson gson) {

        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        this.playerLobby = playerLobby;
        this.gameCenter = gameCenter;
        this.gson = gson;
        LOG.config("GetGameRoute is initialized.");
    }

    /**
     * This function will render the WebCheckers Game page.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     *
     * @return the rendered HTML for the Home page
     */
    public Object handle(Request request, Response response) {

        LOG.finer("GetGameRoute is invoked.");

        // get username from players and put them in the player lobby
        String userName = request.session().attribute(USER_PARAM);
        Player player = playerLobby.getPlayers().get(userName);

        if(!gameCenter.getGameLobby().containsKey(userName) || gameCenter.getGame(userName) == null) {
            response.redirect(WebServer.HOME_URL);
            halt();
        }

        Player playerWhite;
        Player playerRed;

        // checks if player is Red
        if (gameCenter.isRed(player)) {
            playerRed = player;
            playerWhite = gameCenter.getOpponent(player);
        }
        // if white
        else {
            playerWhite = player;
            playerRed = gameCenter.getOpponent(player);
        }

        // get the game
        Game game = gameCenter.getGame(userName);

        // putting values into variables
        Map<String, Object> vm = new HashMap<>();
            vm.put("title", "Game Time!");
            vm.put("currentUser", player);
            vm.put("redPlayer", playerRed);
            vm.put("whitePlayer", playerWhite);
            vm.put("activeColor", game.getActiveColor());

            // red player view
            if(player == playerRed) {
                ArrayList<BoardView> snapshotsRed = game.getRedSnapshots();
                vm.put("board", snapshotsRed.get(snapshotsRed.size()-1));
            }
            // white player view
            else {
                ArrayList<BoardView> snapshotsWhite = game.getWhiteSnapshots();
                vm.put("board", snapshotsWhite.get(snapshotsWhite.size()-1));
            }

            final Map<String, Object> modeOptions = new HashMap<>(2);

            // if player resigned
            String resignedPlayerName = game.getPlayerWhoResigned();
            if (!resignedPlayerName.equals("")){
                modeOptions.put("isGameOver", true);

                // if opponent resigned
                if (!resignedPlayerName.equals(userName)) {
                    modeOptions.put("gameOverMessage", resignedPlayerName + " resigned. You won!");
                }
                // if player resigned
                else {
                    modeOptions.put("gameOverMessage", "You resigned. You technically shouldn't see this message.");
                }
            }
            // if a winner exists
            else if (game.getWinner() != null) {
                modeOptions.put("isGameOver", true);
                modeOptions.put("gameOverMessage", game.getWinner().toString() + " Won!");
            }
            // else game is not over
            else {
                modeOptions.put("isGameOver", false);
            }
            vm.put("modeOptionsAsJSON", gson.toJson(modeOptions));  
            vm.put("viewMode", ViewMode.PLAY);


        // display a user message in the Home page
        vm.put("currentUser", player);

        // render the View
        return templateEngine.render(new ModelAndView(vm , "game.ftl"));
    }
}