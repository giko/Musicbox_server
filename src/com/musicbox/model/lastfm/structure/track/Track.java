package com.musicbox.model.lastfm.structure.track;

import com.musicbox.model.lastfm.structure.artist.Artist;
import com.musicbox.model.lastfm.structure.artist.Image;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "track", schema = "public", catalog = "musicbox")
public class Track {
    @ManyToOne
    @JoinColumn(name = "artistid", nullable = false)
    private Artist artist;
    private String duration;
    @OneToMany(mappedBy = "track", fetch = FetchType.EAGER)
    private Set<Image> image;
    private String listeners;
    @Id
    private String mbid;
    private String name;
    private String playcount;
//    private String url;

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Set<Image> getImage() {
        return image;
    }

    public void setImage(Set<Image> image) {
        this.image = image;
    }

    public String getListeners() {
        return listeners;
    }

    public void setListeners(String listeners) {
        this.listeners = listeners;
    }

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaycount() {
        return playcount;
    }

    public void setPlaycount(String playcount) {
        this.playcount = playcount;
    }
}
