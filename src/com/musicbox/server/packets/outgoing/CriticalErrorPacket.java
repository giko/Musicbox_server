package com.musicbox.server.packets.outgoing;

/**
 * Created with IntelliJ IDEA.
 * User: giko
 * Date: 6/14/13
 * Time: 2:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class CriticalErrorPacket extends AbstractOutgoing{
    String message;

    public CriticalErrorPacket(String message) {
        this.message = message;
    }
}
