package com.musicbox.server.packets.outgoing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.musicbox.BasicSerialisationExclusionStrategy;
import com.musicbox.server.packets.Sendable;
import org.webbitserver.WebSocketConnection;

/**
 * Created with IntelliJ IDEA.
 * User: giko
 * Date: 4/10/13
 * Time: 2:04 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractOutgoing implements Sendable{
    protected static Gson gson = new GsonBuilder().setExclusionStrategies(new BasicSerialisationExclusionStrategy()).create();
    private String action;

    public AbstractOutgoing(){
        action = this.getClass().getSimpleName().replace("Packet", "").toUpperCase();
    }

    @Override
    public void send(WebSocketConnection connection) {
        connection.send(this.toString());
    }

    @Override
    public String toString(){
         return gson.toJson(this);
    }
}
