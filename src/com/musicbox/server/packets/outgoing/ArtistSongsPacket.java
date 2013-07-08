package com.musicbox.server.packets.outgoing;

import com.musicbox.model.lastfm.structure.artist.Artist;

/**
 * Created with IntelliJ IDEA.
 * User: giko
 * Date: 4/10/13
 * Time: 2:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class ArtistSongsPacket extends SongListPacket {
    private Artist artist;

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }
}
