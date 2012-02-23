package com.musicbox.server.packets;

import com.google.gson.Gson;
import com.musicbox.lastfm.structure.artist.Artist;
import com.musicbox.lastfm.structure.track.Track;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Packets {
    protected static Gson json = new Gson();

    static public class Incoming {
        public enum Action {
            LOGIN, SEARCH, LISTENING, LOGINBYTOKEN, LOGINBYCODE, GETSONGBYID, GETURLBYTRACK, CHATMESSAGE, GETTOPSONGSBYARTISTID, GETTOPSONGSBYARTISTNAME
        }

        @NotNull
        public Action getAction() {
            return action;
        }

        public String getMessage() {
            return message;
        }

        @NotNull
        private Action action;
        @Nullable
        private String message;

        public String toJson() {
            return Packets.json.toJson(this);
        }
    }

    static public class Outgoing {
        public enum Action {
            LISTENING, SEARCHRESULT, JOIN, LEAVE, SONGS, TOKEN, MESSAGE, SONGURL, REDIRECTTOVK, LOGINSUCCESS
        }

        public Outgoing(Action caction) {
            this.action = caction;
        }

        public Outgoing() {
            // TODO Auto-generated constructor stub
        }

        @NotNull
        private Action action;
        private String message;
        private List<Track> songs;
        private List<Artist> artists;

        public List<Artist> getArtists() {
            return artists;
        }

        public void setArtists(List<Artist> artists) {
            this.artists = artists;
        }

        public Action getAction() {
            return action;
        }

        public void setAction(Action action) {
            this.action = action;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<Track> getSongs() {
            return songs;
        }

        public void setSongs(List<Track> songs) {
            this.songs = songs;
        }

        public String toJson() {
            return Packets.json.toJson(this);
        }
    }
}
