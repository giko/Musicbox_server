package com.musicbox.server.packets.outgoing;

/**
 * Created with IntelliJ IDEA.
 * User: giko
 * Date: 6/14/13
 * Time: 2:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class LeavePacket extends AbstractOutgoing {
    String name;

    public LeavePacket(String name) {
        this.name = name;
    }
}
