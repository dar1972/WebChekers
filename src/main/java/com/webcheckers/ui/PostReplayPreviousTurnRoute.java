package com.webcheckers.ui;

//Created by Dhruv, commented, and cleaned by Beck
import com.google.gson.Gson;

import com.webcheckers.util.Message;
import spark.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class PostReplayPreviousTurnRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostReplayPreviousTurnRoute.class.getName());

    static final String TITLE = "Game Spectate";
    private final Gson gson;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP
     * requests.
     *
     * @param templateEngine the HTML template rendering engine
     */
    public PostReplayPreviousTurnRoute(TemplateEngine templateEngine, Gson gson) {
        Objects.requireNonNull(templateEngine, "templateEngine is required");
        this.gson = gson;
        LOG.config("PostReplayPreviousTurnRoute is initialized.");
    }

    /**
     * This function will render the WebCheckers Replay page for the previous move.
     * @param request the HTTP request
     * @param response the HTTP response
     * @return json
     */
    @Override
    public Object handle(Request request, Response response) {
        final Session httpSession = request.session();
        LOG.finer("PostReplayPreviousTurnRoute is invoked.");
        Map<String, Object> vm = new HashMap<>();
        vm.put(GetHomeRoute.TITLE_ATTR, TITLE);

        // get the replay index
        int replayIndex = httpSession.attribute("replayIndex");

        // decrement it
        replayIndex--;

        httpSession.attribute("replayIndex", replayIndex);
        Message message;
        //if replayIndex equals the session replayIndex
        if (replayIndex == (int) httpSession.attribute("replayIndex")) {
            message = Message.info("true");
        } else {
            message = Message.info("false");
        }
        // get ready to send message
        String json;
        json = gson.toJson(message);
        return json;
    }
}