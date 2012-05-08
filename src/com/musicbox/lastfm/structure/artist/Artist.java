package com.musicbox.lastfm.structure.artist;

import com.google.gson.annotations.SerializedName;
import com.musicbox.lastfm.LastFmClient;
import com.musicbox.lastfm.structure.track.Track;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Artist {
    private List<Image> image;
    //private int listeners;
    @SerializedName("mbid")
    private String id;
    private String name;
    //private String url;
    private Bio bio;

    public Bio getBio() {
        return bio;
    }

    public void setBio(Bio bio) {
        this.bio = bio;
    }

    @NotNull
    public List<Track> getTopTracks() {
        @NotNull LastFmClient lfclient = new LastFmClient();
        if (this.getId() != null && !this.getId().equals("")) {
            return lfclient.getTopTracksByArtistID(this.getId());
        }
        return lfclient.getTopTracksByArtistName(this.getName());
    }

    @NotNull
    public List<Album> getTopAlbums() {
        @NotNull LastFmClient lfclient = new LastFmClient();
        return lfclient.getTopAlbumsByArtistID(id);
    }

    public List<Image> getImages() {
        return this.image;
    }

    public void setImages(List<Image> image) {
        this.image = image;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
