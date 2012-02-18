package com.musicbox.lastfm.structure.artist;

import com.google.gson.annotations.SerializedName;
import com.musicbox.lastfm.LastFmClient;

import java.util.List;

public class Artist {
    private List<Image> image;
    private String listeners;
    @SerializedName("mbid")
    private String id;
    private String name;
    private String streamable;
    private String url;

    public List<Album> getTopAlbums() {
        LastFmClient lfclient = new LastFmClient();
        return lfclient.GetTopAlbumsByArtistID(id);
    }

    public List<Image> getImages() {
        return this.image;
    }

    public void setImages(List<Image> image) {
        this.image = image;
    }

    public String getListeners() {
        return this.listeners;
    }

    public void setListeners(String listeners) {
        this.listeners = listeners;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreamable() {
        return this.streamable;
    }

    public void setStreamable(String streamable) {
        this.streamable = streamable;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
