package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerLobby;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.TemplateEngine;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;


@Tag("UI-tier")
class WebServerTest {

    TemplateEngine engine = mock(TemplateEngine.class);
    PlayerLobby playerLobby = new PlayerLobby();
    GameCenter center = new GameCenter(playerLobby);
    Gson gson = new Gson();



    @Test
    void constructor() {
        WebServer web = new WebServer(engine, gson, playerLobby, center);
        assertNotNull(web);
    }
}