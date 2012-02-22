package com.musicbox.server;

import com.musicbox.lastfm.structure.artist.Artist;
import com.musicbox.lastfm.structure.track.Track;
import com.musicbox.weborama.structure.SearchResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Packets {
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
        private SearchResult result;
        private String message;
        private String username;
        private List<Track> songs;

        public List<Artist> getArtists() {
            return artists;
        }

        public void setArtists(List<Artist> artists) {
            this.artists = artists;
        }

        private List<Artist> artists;

        public Action getAction() {
            return action;
        }

        public void setAction(Action action) {
            this.action = action;
        }

        public SearchResult getResult() {
            return result;
        }

        public void setResult(SearchResult result) {
            this.result = result;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public List<Track> getSongs() {
            return songs;
        }

        public void setSongs(List<Track> songs) {
            this.songs = songs;
        }
    }
}
