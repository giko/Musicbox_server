package com.musicbox.server;

import com.musicbox.server.db.Connection;
import org.webbitserver.WebServer;
import org.webbitserver.handler.StaticFileHandler;
import org.webbitserver.handler.logging.LoggingHandler;
import org.webbitserver.handler.logging.SimpleLogSink;

import java.io.File;

import static org.webbitserver.WebServers.createWebServer;

public class ServerMain {


    public static void main(String[] args) throws Exception {
        Connection.getEntityManager().close();

        int port = 80;
        WebServer webServer = createWebServer(port);

        if (Config.getInstance().isWebbitdebug()) {
            webServer.add(new LoggingHandler(
                    new SimpleLogSink(MusicboxServer.USERNAME_KEY)));
        }

        webServer.add("/musicbox", new MusicboxServer())
                .add(new StaticFileHandler(
                        new File(ServerMain.class.getResource("/content").toURI())));

        webServer.start().get();

    }
}