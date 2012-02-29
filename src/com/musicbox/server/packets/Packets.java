package com.musicbox.server.packets;

import com.google.gson.Gson;
import com.musicbox.lastfm.structure.artist.Artist;
import com.musicbox.lastfm.structure.tag.Tag;
import com.musicbox.lastfm.structure.track.Track;
import com.musicbox.vkontakte.structure.audio.Audio;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Packets {
    protected static Gson json = new Gson();

    static public class Incoming {
        public enum Action {
            GETAUDIOBYTRACK, LOGIN, SEARCH, LISTENING, LOGINBYTOKEN, LOGINBYCODE, GETSONGBYID, GETURLBYTRACK, CHATMESSAGE, GETTOPSONGSBYARTISTID, GETTOPSONGSBYARTISTNAME, ADDTOLIBRARY, SEARCHBYTAG
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
            LISTENING, SEARCHRESULT, JOIN, LEAVE, SONGS, TOKEN, MESSAGE, SONGURL, REDIRECTTOVK, LOGINSUCCESS ,AUDIO
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
        private List<Tag> tags;
        private Audio audio;

        public Audio getAudio() {
            return audio;
        }

        public void setAudio(Audio audio) {
            this.audio = audio;
        }

        public List<Tag> getTags() {
            return tags;
        }

        public void setTags(List<Tag> tags) {
            this.tags = tags;
        }

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
