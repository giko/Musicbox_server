package com.musicbox.model.vkontakte;

import com.google.gson.Gson;
import com.musicbox.Cache;
import com.musicbox.CacheAllocator;
import com.musicbox.WebWorker;
import com.musicbox.model.vkontakte.structure.audio.Audio;
import com.musicbox.model.vkontakte.structure.audio.AudioSearch;
import com.musicbox.model.vkontakte.structure.profiles.Profile;
import com.musicbox.model.vkontakte.structure.profiles.ProfileSearch;
import com.musicbox.server.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.URLEncoder;

public class VkontakteClient {
    @NotNull
    private static final Cache cache = new Cache();
    private static final String appid = Config.getInstance().getVkappid();
    private static final String appsecret = Config.getInstance().getVksecretkey();
    @NotNull
    private final OAuthToken oauth;
    @NotNull
    private final Gson json = new Gson();


    public VkontakteClient(@NotNull final OAuthToken token) {
        this.oauth = token;
    }

    @NotNull
    public static Cache getCache() {
        return cache;
    }

    public static OAuthToken getOauthTokenByCode(@NotNull final String code) {
        @NotNull String query = "https://oauth.vk.com/access_token?client_id=" + appid + "&client_secret=" + appsecret + "&code=" + code + "&redirect_uri=" + Config.getInstance().getServerIP();
        @NotNull Gson json = new Gson();
        return json.fromJson(
                new InputStreamReader(WebWorker.retrieveStream(query)),
                OAuthToken.class);
    }

    @NotNull
    public final Audio getAudioByTrack(@NotNull final String track) {
        @NotNull CacheAllocator cacheAllocator = cache.getAllocator("getURLByTrack", track + oauth.getUser_id(), Audio.class);

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
    public final Profile getProfileById(@NotNull final int id) {
        @NotNull CacheAllocator cacheAllocator = cache.getAllocator("getProfileById", String.valueOf(id), Profile.class);

        if (!cacheAllocator.exists()) {
            Profile profile = this.json
                    .fromJson(
                            retrieveReader(
                                    ("getProfiles?uids=" + String.valueOf(id)) + "&fields=photo_big,sex,bdate,city,country",
                                    this.oauth.getAccess_token()), ProfileSearch.class).getResponse()
                    .get(0);
            profile.setToken(this.oauth);
            this.oauth.setProfile(profile);

            cacheAllocator.cacheObject(profile);
            return profile;
        }
        return (Profile) cacheAllocator.getObject();
    }

    public boolean addSongToFavoriteByTrack(@NotNull String query) {
        @NotNull Audio audio = getAudioByTrack(query);
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
        String url = (("https://api.vk.com/method/" + query) + "&access_token=") + token;
        @Nullable InputStream source = WebWorker.retrieveStream(url);
        try {
            return new InputStreamReader(source, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
