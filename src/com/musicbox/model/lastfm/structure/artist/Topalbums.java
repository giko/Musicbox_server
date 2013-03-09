package com.musicbox.model.lastfm.structure.artist;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Topalbums {
    @SerializedName("album")
    private List<Album> albums;

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

}
