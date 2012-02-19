package com.musicbox.lastfm.structure.artist;

import com.google.gson.annotations.SerializedName;
import com.musicbox.lastfm.LastFmClient;
import com.musicbox.lastfm.structure.track.Track;

import java.util.List;

public class Artist {
    private List<Image> image;
    private int listeners;
    @SerializedName("mbid")
    private String id;
    private String name;
    private String url;

    public List<Track> getTopTracks() {
        LastFmClient lfclient = new LastFmClient();
        if (this.getId() != null && !this.getId().equals("")) {
            return lfclient.getTopTracksByArtistID(this.getId());
        }
        return lfclient.getTopTracksByArtistName(this.getName());
    }

    public List<Album> getTopAlbums() {
        LastFmClient lfclient = new LastFmClient();
        return lfclient.getTopAlbumsByArtistID(id);
    }

    public List<Image> getImages() {
        return this.image;
    }

    public void setImages(List<Image> image) {
        this.image = image;
    }

    public int getListeners() {
        return this.listeners;
    }

    public void setListeners(int listeners) {
        this.listeners = listeners;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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
