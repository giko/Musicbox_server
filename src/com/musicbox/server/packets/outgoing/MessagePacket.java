package com.musicbox.server.packets.outgoing;

/**
 * Created with IntelliJ IDEA.
 * User: giko
 * Date: 6/14/13
 * Time: 2:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessagePacket extends AbstractOutgoing {
    String message;

    public MessagePacket() {
    }

    public MessagePacket(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
