package com.musicbox.server.packets.handlers;

import com.google.gson.Gson;
import com.musicbox.CacheAllocator;
import com.musicbox.model.vkontakte.VkontakteClient;
import com.musicbox.model.vkontakte.structure.audio.Audio;
import com.musicbox.model.vkontakte.structure.audio.AudioSearch;
import com.musicbox.server.MusicboxServer;
import com.musicbox.server.packets.ExecuteRequest;
import com.musicbox.server.packets.Packets;
import com.musicbox.server.packets.outgoing.AudioPacket;
import com.musicbox.server.packets.outgoing.ExecuteRequestPacket;
import org.jetbrains.annotations.NotNull;
import org.webbitserver.WebSocketConnection;

/**
 * Created by IntelliJ IDEA.
 * User: giko
 * Date: 29.02.12
 * Time: 22:53
 */
class GetAudioByTrackExecuteRespond {
    private String query;
    private AudioSearch data;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public AudioSearch getData() {
        return data;
    }

    public void setData(AudioSearch data) {
        this.data = data;
    }
}

public class GetAudioByTrack extends AbstractHandler {
    public GetAudioByTrack(MusicboxServer server) {
        super(server);
    }

    @Override
    public void HandlePacket(@NotNull WebSocketConnection connection, @NotNull Packets.Incoming incoming) {
        @NotNull
        CacheAllocator cacheAllocator = VkontakteClient.getCache().getAllocator("GetAudioByTrack",
                incoming.getMessage() + connections_.get(connection).getId() + getIpByConnection(connection), Audio.class);

        if (!cacheAllocator.exists()) {
            ExecuteRequestPacket packet = new ExecuteRequestPacket();

            ExecuteRequest request = new ExecuteRequest();
            request.setAction(Packets.Incoming.Action.GETAUDIOBYTRACK);
            request.setUrl("https://api.vkontakte.ru/method/execute");
            request.getData().put("code", "return API.audio.search({\"q\":\"" + incoming.getMessage() + "\",\"count\":1, \"sort\":2, \"lyrics\":1})[1];");
            request.getData().put("access_token", connections_.get(connection).getToken().getAccess_token());
            request.getData().put("callback", "JSON_CALLBACK");

            packet.setMessage(incoming.getMessage());
            packet.setRequest(request);
            packet.send(connection);
            return;
        }

        (new AudioPacket((Audio) cacheAllocator.getObject())).send(connection);
    }

    @Override
    public void HandleExecuteRequest(@NotNull WebSocketConnection connection, @NotNull String result) {
        Gson json = new Gson();

        @NotNull
        GetAudioByTrackExecuteRespond respond = json.fromJson(result, GetAudioByTrackExecuteRespond.class);

        @NotNull
        CacheAllocator cacheAllocator = VkontakteClient.getCache().getAllocator("GetAudioByTrack",
                respond.getQuery() + connections_.get(connection).getId() + getIpByConnection(connection), Audio.class, 10);

        cacheAllocator.cacheObject(respond.getData().getResponse());

        (new AudioPacket((Audio) cacheAllocator.getObject())).send(connection);
    }
}
