package com.musicbox.lastfm.structure.track;

import com.musicbox.lastfm.structure.artist.Artist;
import com.musicbox.lastfm.structure.artist.Image;

import java.util.List;

public class Track {
    private Artist artist;
    private String duration;
    private List<Image> image;
    private String listeners;
    private String mbid;
    private String name;
    private String playcount;
    private String url;


    public Artist getArtist() {
        return this.artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public List getImage() {
        return this.image;
    }

    public void setImage(List image) {
        this.image = image;
    }

    public String getListeners() {
        return this.listeners;
    }

    public void setListeners(String listeners) {
        this.listeners = listeners;
    }

    public String getMbid() {
        return this.mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaycount() {
        return this.playcount;
    }

    public void setPlaycount(String playcount) {
        this.playcount = playcount;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
