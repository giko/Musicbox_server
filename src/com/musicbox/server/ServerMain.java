package com.musicbox.server;

import com.musicbox.lastfm.LastFmClient;
import org.webbitserver.WebServer;
import org.webbitserver.handler.StaticFileHandler;
import org.webbitserver.handler.logging.LoggingHandler;
import org.webbitserver.handler.logging.SimpleLogSink;

import static org.webbitserver.WebServers.createWebServer;

public class ServerMain {

    public static void main(String[] args) throws Exception {
        WebServer webServer = createWebServer(9876)
                .add(new LoggingHandler(
                        new SimpleLogSink(Musicbox.USERNAME_KEY)))
                .add("/musicbox", new Musicbox())
                .add(new StaticFileHandler(
                        "./content")).start()
                .get();

        System.out.println("Webserver on: " + webServer.getUri());
    }
}