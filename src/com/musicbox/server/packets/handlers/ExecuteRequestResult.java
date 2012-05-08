package com.musicbox.server.packets.handlers;

import com.google.gson.Gson;
import com.musicbox.server.MusicboxServer;
import com.musicbox.server.packets.ExecuteRequsetResponse;
import com.musicbox.server.packets.Packets;
import org.jetbrains.annotations.NotNull;
import org.webbitserver.WebSocketConnection;

/**
 * Created by IntelliJ IDEA.
 * User: giko
 * Date: 08.05.12
 * Time: 18:00
 */

public class ExecuteRequestResult extends AbstractHandler {

    public ExecuteRequestResult(MusicboxServer server) {
        super(server);
    }

    @Override
    public void HandlePacket(WebSocketConnection connection, @NotNull Packets.Incoming incoming) {
        Gson json = new Gson();
        ExecuteRequsetResponse response = json.fromJson(incoming.getMessage(), ExecuteRequsetResponse.class);
        server.getPackethandlers().get(response.getAction()).HandleExecuteRequest(connection, response.getResult());
    }
}