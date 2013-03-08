package com.musicbox.lastfm.structure.artist;

import com.google.gson.annotations.SerializedName;
import com.musicbox.lastfm.LastFmClient;
import com.musicbox.lastfm.structure.track.Track;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "artist", schema = "public", catalog = "musicbox")
public class Artist implements Serializable {
    //private int listeners;
    @SerializedName("mbid")
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "name")
    private String name;
    //private String url;
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "artist", cascade = CascadeType.ALL)
    private Bio bio;
    @SerializedName("image")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "artist", cascade = CascadeType.ALL)
    private Set<Image> images;

    @Nullable
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

    public Set<Image> getImages() {
        return this.images;
    }

    public void setImages(Set<Image> image) {
        this.images = image;
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

    public void setData() {
        for (Image image : images) {
            image.setArtist(this);
        }
        if (bio != null) {
            bio.setArtist(this);
        }
    }
}
