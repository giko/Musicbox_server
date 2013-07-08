package com.musicbox.server.packets;

import org.webbitserver.WebSocketConnection;

/**
 * Created with IntelliJ IDEA.
 * User: giko
 * Date: 4/10/13
 * Time: 2:03 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Sendable {
    public void send(WebSocketConnection connection);
}
