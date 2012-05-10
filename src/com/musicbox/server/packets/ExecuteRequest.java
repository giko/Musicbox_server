package com.musicbox.server.packets;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: giko
 * Date: 08.05.12
 * Time: 16:45
 */
public class ExecuteRequest {
    private String url;
    private Packets.Incoming.Action action;
    private Map<String, String> data = new HashMap<String, String>();

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

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
