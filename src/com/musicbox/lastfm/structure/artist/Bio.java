package com.musicbox.lastfm.structure.artist;

import com.musicbox.ExcludeFromSerialisation;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: giko
 * Date: 01.05.12
 * Time: 15:17
 */
@Entity
@Table(name = "bio", schema = "public", catalog = "musicbox")
public class Bio implements Serializable {
    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    @ExcludeFromSerialisation
    private Artist artist;
    private String published;
    private String summary;
    //private String content;
    @Id
    @Column(name = "id")
    private Integer id;

    public Artist getArtist() {
        return this.artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
