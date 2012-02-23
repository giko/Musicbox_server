package com.musicbox.vkontakte.structure.audio;

/**
 * Created by IntelliJ IDEA.
 * User: giko
 * Date: 18.02.12
 * Time: 10:51
 */
public class Audio {
    private String aid;
    private String owner_id;
    private String artist;
    private String title;
    private String duration;
    private String url;

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
