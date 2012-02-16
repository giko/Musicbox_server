package com.musicbox.server;

import com.google.gson.Gson;
import com.musicbox.weborama.WeboramaClient;
import com.musicbox.weborama.structure.SearchResult;
import com.musicbox.weborama.structure.ServerOutput;

import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;
import org.webbitserver.WebSocketHandler;

import java.util.HashSet;
import java.util.Set;

public class Musicbox extends BaseWebSocketHandler {

	private final Gson json = new Gson();
	private static WeboramaClient wclient = new WeboramaClient();

	public static final String USERNAME_KEY = "username";

	static class Incoming {
		enum Action {
			SEARCH, LISTENING, LOGIN
		}

		Action action;
		String loginUsername;
		String message;
	}

	static class Outgoing {
		enum Action {
			LISTENING, SEARCHRESULT, JOIN, LEAVE
		}

		Action action;
		SearchResult result;
		String message;
		String username;
	}
	
    private void login(WebSocketConnection connection, String username) {
        connection.data(USERNAME_KEY, username); // associate username with connection

        Outgoing outgoing = new Outgoing();
        outgoing.action = Outgoing.Action.JOIN;
        outgoing.username = username;
        broadcast(outgoing);
    }

	private Set<WebSocketConnection> connections = new HashSet<WebSocketConnection>();

	@Override
	public void onOpen(WebSocketConnection connection) throws Exception {
		connections.add(connection);
	}

	@Override
	public void onMessage(WebSocketConnection connection, String msg)
			throws Exception {
		Incoming incoming = json.fromJson(msg, Incoming.class);
		switch (incoming.action) {
		case LOGIN:
			System.out.println(incoming.loginUsername);
			login(connection, incoming.loginUsername);
			break;
		case SEARCH:
			Outgoing packet = new Outgoing();
			packet.action = Outgoing.Action.SEARCHRESULT;
			packet.result = wclient.Search(incoming.message);
			connection.send(this.json.toJson(packet));
			break;
		}
	}
	
    private void broadcast(Outgoing outgoing) {
        String jsonStr = this.json.toJson(outgoing);
        for (WebSocketConnection connection : connections) {
            if (connection.data(USERNAME_KEY) != null) { // only broadcast to those who have completed login
                connection.send(jsonStr);
            }
        }
    }

    
	@Override
	public void onClose(WebSocketConnection connection) throws Exception {
		String username = (String) connection.data(USERNAME_KEY);
		if (username != null) {
			Outgoing outgoing = new Outgoing();
			outgoing.action = Outgoing.Action.LEAVE;
			outgoing.username = username;
			broadcast(outgoing);
		}
		connections.remove(connection);
	}

}
