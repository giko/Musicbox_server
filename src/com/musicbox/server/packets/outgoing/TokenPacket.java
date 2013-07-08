package com.musicbox.server.packets.outgoing;

/**
 * Created with IntelliJ IDEA.
 * User: giko
 * Date: 6/14/13
 * Time: 2:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class TokenPacket extends AbstractOutgoing {
    String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
