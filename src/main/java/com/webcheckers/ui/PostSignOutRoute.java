package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;

// File created by Beck, code by Marcus, code adjusted by Kelly, commented, and cleaned by Beck

public class PostSignOutRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostSignInRoute.class.getName());

    static final String USER_PARAM = "userName";

    private final TemplateEngine templateEngine;
    private final PlayerLobby playerLobby;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP
     * requests.
     *
     * @param templateEngine the HTML template rendering engine
     */
    public PostSignOutRoute(final TemplateEngine templateEngine, final PlayerLobby playerLobby) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        this.playerLobby = playerLobby;
        LOG.config("PostSignOutRoute is initialized.");
    }

    @Override
    public Object handle(Request request, Response response) {
        
        LOG.finer("PostSignOutRoute is invoked.");

        String userName = request.session().attribute(USER_PARAM);

        playerLobby.removeFromLobby(playerLobby.getPlayers().get(userName));

        request.session().attribute(USER_PARAM, null);
        response.redirect(WebServer.HOME_URL);
        halt();

        // used to put in title name, this varies depending on which page it is
        Map<String, Object> vm = new HashMap<>();
        vm.put("title", "We will never reach here");

        // display a user message in the Home page        

        return templateEngine.render(new ModelAndView(vm , "home.ftl"));
    }
}
