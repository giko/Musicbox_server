package com.musicbox.model.lastfm.structure.artist;

import com.google.gson.annotations.SerializedName;
import com.musicbox.ExcludeFromSerialisation;
import com.musicbox.model.lastfm.structure.track.Track;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "image", schema = "public", catalog = "musicbox")
public class Image implements Serializable {
    @SerializedName("#text")
    @Id
    private String link;
    private String size;
    @ManyToOne
    @JoinColumn(name = "artistid", nullable = false)
    @ExcludeFromSerialisation
    private Artist artist;
    @ManyToOne
    @JoinColumn(name = "trackid", nullable = true)
    @ExcludeFromSerialisation
    private Track track;

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public Artist getArtist() {
        return this.artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    @Column(name = "link")
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Column(name = "size")
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

}
