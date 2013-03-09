package com.musicbox.model.lastfm.structure.artist;

import com.musicbox.ExcludeFromSerialisation;
import org.hibernate.annotations.GenericGenerator;

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
    @Column(name = "published", columnDefinition="TEXT")
    private String published;
    @Column(name = "summary", columnDefinition="TEXT")
    private String summary;
    //private String content;
    @Id
    @GeneratedValue(generator = "foreign")
    @GenericGenerator(
            name = "foreign",
            strategy = "foreign",
            parameters = {@org.hibernate.annotations.Parameter(name = "property", value = "artist")})
    @Column(name = "id")
    private Integer id;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
