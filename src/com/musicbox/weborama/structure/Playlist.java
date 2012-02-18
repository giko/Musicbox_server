package com.musicbox.weborama.structure;

import java.util.List;

public class Playlist {
    private String identifier;
    private List<TrackList> trackList;
    private String type;

    public String getIdentifier() {
        return identifier;
    }

    public List<TrackList> getTrackList() {
        return trackList;
    }

    public String getType() {
        return type;
    }
}
