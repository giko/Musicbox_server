package com.musicbox.model.lastfm;

import com.musicbox.model.lastfm.structure.artist.Artist;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: giko
 * Date: 3/9/13
 * Time: 10:24 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "topartist", schema = "public", catalog = "musicbox", uniqueConstraints = {@UniqueConstraint(columnNames = {"artistid"})})
public class TopArtistsEntity {
    @Id
    @GeneratedValue
    private int id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "artistid", nullable = false)
    private Artist artist;

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
