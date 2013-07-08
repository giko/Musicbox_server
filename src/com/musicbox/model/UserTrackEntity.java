package com.musicbox.model;

import com.musicbox.model.lastfm.structure.track.Track;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: giko
 * Date: 3/17/13
 * Time: 12:47 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "usertrack", schema = "public", catalog = "musicbox")
public class UserTrackEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
    @OneToOne(cascade = CascadeType.ALL)
    private Track track;
    @Column(name = "playscount")
    private int playsCount;
    @OneToOne
    private UserEntity user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public int getPlaysCount() {
        return playsCount;
    }

    public void setPlaysCount(int playsCount) {
        this.playsCount = playsCount;
    }
}
