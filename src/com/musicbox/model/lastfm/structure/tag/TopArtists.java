package com.musicbox.model.lastfm.structure.tag;

import com.musicbox.model.lastfm.structure.artist.Artist;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Giko
 * Date: 24.02.12
 * Time: 10:41
 * To change this template use File | Settings | File Templates.
 */
public class TopArtists {
    List<Artist> artist;

    public List<Artist> getArtist() {
        return artist;
    }

    public void setArtist(List<Artist> artist) {
        this.artist = artist;
    }
}
