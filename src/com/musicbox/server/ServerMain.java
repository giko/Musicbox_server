package com.musicbox.server;

import org.webbitserver.WebServer;
import org.webbitserver.handler.StaticFileHandler;
import org.webbitserver.handler.logging.LoggingHandler;
import org.webbitserver.handler.logging.SimpleLogSink;

import static org.webbitserver.WebServers.createWebServer;

public class ServerMain {

    public static void main(String[] args) throws Exception {
        int port = 80;
        WebServer webServer = createWebServer(port)
                .add(new LoggingHandler(
                        new SimpleLogSink(MusicboxServer.USERNAME_KEY)))
                .add("/musicbox", new MusicboxServer())
                .add(new StaticFileHandler(
                        "./content")).start()
                .get();
    }
}