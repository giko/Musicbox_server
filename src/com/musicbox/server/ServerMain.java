package com.musicbox.server;

import java.util.HashMap;
import java.util.TreeMap;

import org.webbitserver.WebServer;
import org.webbitserver.handler.StaticFileHandler;
import org.webbitserver.handler.logging.LoggingHandler;
import org.webbitserver.handler.logging.SimpleLogSink;
import com.musicbox.lastfm.LastFmClient;
import com.musicbox.vkontakte.VkontakteClient;

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
		
		LastFmClient lfclient = new LastFmClient();
		System.out.println(lfclient.SearchArtist("rise against").get(0).getImages().get(4).getLink());
	}
}