package com.musicbox.vkontakte.structure.audio;

import com.google.gson.annotations.SerializedName;

/**
 * Created by IntelliJ IDEA.
 * User: giko
 * Date: 18.02.12
 * Time: 10:51
 */
public class Audio {
    @SerializedName("")
    private int count;
    @SerializedName("aid")
    private int id;
    private int owner_id;
    private String artist;
    private String title;
    private int duration;
    private String url;

    public int getOwner_id() {
        return owner_id;
    }

    public int getId() {
        return id;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public int getDuration() {
        return duration;
    }

    public String getUrl() {
        return url;
    }
}
