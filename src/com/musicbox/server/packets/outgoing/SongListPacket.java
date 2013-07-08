package com.musicbox.server.packets.outgoing;

import com.musicbox.model.lastfm.structure.track.Track;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: giko
 * Date: 4/10/13
 * Time: 2:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class SongListPacket extends AbstractOutgoing{
    List<Track> songs;

    public List<Track> getSongs() {
        return songs;
    }

    public void setSongs(List<Track> songs) {
        this.songs = songs;
    }
}
