package com.musicbox.server;

import com.musicbox.lastfm.structure.artist.Artist;
import com.musicbox.lastfm.structure.track.Track;
import com.musicbox.weborama.structure.SearchResult;
import com.musicbox.weborama.structure.TrackList;

import java.util.List;

public class Packets {
    static public class Incoming {
        public enum Action {
            SEARCH, LISTENING, LOGINBYTOKEN, LOGINBYCODE, GETSONGBYID, GETURLBYTRACK, CHATMESSAGE, GETTOPSONGSBYARTISTID
        }

        private Action action;
        private String loginUsername;
        private String message;


        public Action getAction() {
            return action;
        }

        public void setAction(Action action) {
            this.action = action;
        }

        public String getLoginUsername() {
            return loginUsername;
        }

        public void setLoginUsername(String loginUsername) {
            this.loginUsername = loginUsername;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    static public class Outgoing {
        public enum Action {
            LISTENING, SEARCHRESULT, JOIN, LEAVE, SONGS, TOKEN, MESSAGE, SONGURL
        }

        public Outgoing(Action caction) {
            this.action = caction;
        }

        public Outgoing() {
            // TODO Auto-generated constructor stub
        }

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
