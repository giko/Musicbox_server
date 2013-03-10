package com.musicbox.model.lastfm.structure.artist;

import com.google.gson.annotations.SerializedName;
import com.musicbox.model.lastfm.LastFmClient;
import com.musicbox.model.lastfm.structure.track.Track;
import com.musicbox.server.logic.tools.MD5;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "artist", schema = "public", catalog = "musicbox", uniqueConstraints = {@UniqueConstraint(columnNames = {"mbid"})})
public class Artist implements Serializable {
    //private int listeners;
    @Column(name = "name")
    private String name;
    @Id
    @Column(name = "mbid")
    private String mbid;
    //private String url;
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "artist", cascade = CascadeType.ALL)
    private Bio bio;
    @SerializedName("image")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "artist", cascade = CascadeType.ALL)
    private Set<Image> images;

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

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
        if (this.getMbid() != null && !this.getMbid().equals("")) {
            return lfclient.getTopTracksByArtistID(this.getMbid());
        }
        return lfclient.getTopTracksByArtistName(this.getName());
    }

    @NotNull
    public List<Album> getTopAlbums() {
        @NotNull LastFmClient lfclient = new LastFmClient();
        return lfclient.getTopAlbumsByArtistID(mbid);
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

    public void setData() {
        if (getMbid().equals("")) {
            setMbid(MD5.getMD5(getName()));
        }
        for (Image image : images) {
            image.setArtist(this);
        }
        if (bio != null) {
            bio.setArtist(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Artist artist = (Artist) o;

        if (bio != null ? !bio.equals(artist.bio) : artist.bio != null) return false;
        if (images != null ? !images.equals(artist.images) : artist.images != null) return false;
        if (mbid != null ? !mbid.equals(artist.mbid) : artist.mbid != null) return false;
        if (!name.equals(artist.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (mbid != null ? mbid.hashCode() : 0);
        result = 31 * result + (bio != null ? bio.hashCode() : 0);
        result = 31 * result + (images != null ? images.hashCode() : 0);
        return result;
    }
}
