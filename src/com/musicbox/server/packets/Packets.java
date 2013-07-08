package com.musicbox.server.packets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.musicbox.BasicSerialisationExclusionStrategy;
import com.musicbox.model.UserEntity;
import com.musicbox.model.lastfm.structure.artist.Artist;
import com.musicbox.model.lastfm.structure.tag.Tag;
import com.musicbox.model.lastfm.structure.track.Track;
import com.musicbox.model.vkontakte.structure.audio.Audio;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Packets {
    static public class Incoming {
        @NotNull
        private Incoming.Action action;
        @Nullable
        private String message;

        @Nullable
        public Incoming.Action getAction() {
            return action;
        }

        @Nullable
        public String getMessage() {
            return message;
        }

        public enum Action {
            GETUSER, EXECUTEREQUESTRESULT, SEARCHSIMILARARTISTSBYNAME, GETAUDIOBYTRACK, LOGIN, SEARCH, LISTENING, LOGINBYTOKEN, LOGINBYCODE, GETSONGBYID, GETURLBYTRACK, CHATMESSAGE, GETTOPSONGSBYARTISTID, GETTOPSONGSBYARTISTNAME, ADDTOLIBRARY, SEARCHBYTAG
        }
    }
}
