package com.musicbox.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: giko
 * Date: 3/17/13
 * Time: 1:05 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "playlist", schema = "public", catalog = "musicbox")
public class PlaylistEntity {
    @OneToMany
    List<UserTrackEntity> tracks;
    @Id
    @GeneratedValue
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "visible")
    private boolean visible;

    public List<UserTrackEntity> getTracks() {
        return tracks;
    }

    public void setTracks(List<UserTrackEntity> tracks) {
        this.tracks = tracks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
