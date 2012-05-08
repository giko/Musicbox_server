package com.musicbox.server.packets;

/**
 * Created by IntelliJ IDEA.
 * User: giko
 * Date: 08.05.12
 * Time: 18:01
 */
public class ExecuteRequsetResponse {
    private Packets.Incoming.Action action;
    private String result;

    public Packets.Incoming.Action getAction() {
        return action;
    }

    public void setAction(Packets.Incoming.Action action) {
        this.action = action;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
