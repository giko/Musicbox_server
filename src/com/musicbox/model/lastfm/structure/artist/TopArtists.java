package com.musicbox.model.lastfm.structure.artist;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Giko
 * Date: 20.02.12
 * Time: 12:52
 * To change this template use File | Settings | File Templates.
 */
public class TopArtists {
    private List<Artist> artist;

    public List<Artist> getArtist() {
        return artist;
    }

    public void setArtist(List<Artist> artist) {
        this.artist = artist;
    }
}
