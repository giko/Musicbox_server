package com.musicbox.server.packets;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: giko
 * Date: 08.05.12
 * Time: 16:45
 */
public class ExecuteRequest {
    private String url;
    private Packets.Incoming.Action action;
    private HashMap<String, String> data = new HashMap<String, String>();

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Packets.Incoming.Action getAction() {
        return action;
    }

    public void setAction(Packets.Incoming.Action action) {
        this.action = action;
    }

    public HashMap<String, String> getData() {
        return data;
    }

    public void setData(HashMap<String, String> data) {
        this.data = data;
    }
}
