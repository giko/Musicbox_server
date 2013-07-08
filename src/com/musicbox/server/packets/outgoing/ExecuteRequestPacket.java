package com.musicbox.server.packets.outgoing;

import com.musicbox.server.packets.ExecuteRequest;

/**
 * Created with IntelliJ IDEA.
 * User: giko
 * Date: 6/14/13
 * Time: 2:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExecuteRequestPacket extends MessagePacket {
    ExecuteRequest request;

    public ExecuteRequest getRequest() {
        return request;
    }

    public void setRequest(ExecuteRequest request) {
        this.request = request;
    }
}
