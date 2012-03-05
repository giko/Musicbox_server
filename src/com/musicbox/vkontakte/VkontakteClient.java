package com.musicbox.vkontakte;

import com.google.gson.Gson;
import com.musicbox.Cache;
import com.musicbox.CacheAllocator;
import com.musicbox.WebWorker;
import com.musicbox.server.Config;
import com.musicbox.vkontakte.structure.audio.Audio;
import com.musicbox.vkontakte.structure.audio.AudioSearch;
import com.musicbox.vkontakte.structure.profiles.Profile;
import com.musicbox.vkontakte.structure.profiles.ProfileSearch;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.URLEncoder;

public class VkontakteClient {
    @NotNull
    private final OAuthToken oauth;
    @NotNull
    private final Gson json = new Gson();
    @NotNull
    private static final Cache cache = new Cache();

    private static final String appid = Config.getInstance().getVkappid();
    private static final String appsecret = Config.getInstance().getVksecretkey();

    public VkontakteClient(final OAuthToken token) {
        this.oauth = token;
    }

    public static OAuthToken getOauthTokenByCode(@NotNull final String code) {
        String query = "https://oauth.vkontakte.ru/access_token?client_id=" + appid + "&client_secret=" + appsecret + "&code="
                .concat(code);
        Gson json = new Gson();
        return json.fromJson(
                new InputStreamReader(WebWorker.retrieveStream(query)),
                OAuthToken.class);
    }

    @NotNull
    public final Audio getAudioByTrack(@NotNull final String track) {
        CacheAllocator cacheAllocator = cache.getAllocator("getURLByTrack", track, Audio.class);

        if (!cacheAllocator.exists()) {
            Audio audio = this.json.fromJson(retrieveReader("execute?code=" + URLEncoder.encode("return API.audio.search({\"q\":\"" + track + "\",\"count\":1, \"sort\":2})[1];")), AudioSearch.class).getResponse();
            cacheAllocator.cacheObject(audio);
            return audio;
        }
        return (Audio) cacheAllocator.getObject();
    }

    @NotNull
    public final Profile getProfile() {
        return getProfileById(oauth.getUser_id());
    }

    @NotNull
    public final Profile getProfileById(final int id) {
        CacheAllocator cacheAllocator = cache.getAllocator("getProfileById", String.valueOf(id), Profile.class);

        if (!cacheAllocator.exists()) {
            Profile profile = this.json
                    .fromJson(
                            retrieveReader(
                                    "getProfiles?uids="
                                            .concat(String.valueOf(id))
                                            .concat("&fields=photo_big,sex,bdate,city,country"),
                                    this.oauth.getAccess_token()), ProfileSearch.class).getResponse()
                    .get(0);
            profile.setToken(this.oauth);

            cacheAllocator.cacheObject(profile);
            return profile;
        }
        return (Profile) cacheAllocator.getObject();
    }

    public boolean addSongToFavoriteByTrack(String query) {
        Audio audio = getAudioByTrack(query);
        try {
            retrieveReader("audio.add?aid=" + audio.getAid() + "&oid=" + audio.getOwner_id()).read();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Nullable
    private Reader retrieveReader(@NotNull String query) {
        return retrieveReader(query, this.oauth.getAccess_token());
    }

    @Nullable
    private Reader retrieveReader(@NotNull final String query, @NotNull final String token) {
        String url = "https://api.vkontakte.ru/method/".concat(query)
                .concat("&access_token=").concat(token);
        System.out.println(url);

        InputStream source = WebWorker.retrieveStream(url);
        try {
            return new InputStreamReader(source, "utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
