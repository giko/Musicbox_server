package com.musicbox.server.packets.outgoing;

import com.musicbox.model.lastfm.structure.artist.Artist;
import com.musicbox.model.lastfm.structure.tag.Tag;
import com.musicbox.model.lastfm.structure.track.Track;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: giko
 * Date: 6/14/13
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchResultPacket extends AbstractOutgoing {
    List<Track> songs;
    List<Artist> artists;
    List<Tag> tags;

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Track> getSongs() {
        return songs;
    }

    public void setSongs(List<Track> songs) {
        this.songs = songs;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }
}
