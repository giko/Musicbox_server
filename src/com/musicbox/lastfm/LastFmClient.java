package com.musicbox.lastfm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.musicbox.Cache;
import com.musicbox.CacheAllocator;
import com.musicbox.WebWorker;
import com.musicbox.lastfm.structure.artist.*;
import com.musicbox.lastfm.structure.track.ArtistTopTracksSearchResult;
import com.musicbox.lastfm.structure.track.Track;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class LastFmClient {
    final Type locationInfoListType = new TypeToken<List<Artist>>() {
    }.getType();
    final Gson json = new GsonBuilder()
            .registerTypeAdapter(locationInfoListType, new ArtistTypeAdapter())
            .create();
    @NotNull
    private static final Cache cache = new Cache();

    @NotNull
    public List<Track> getTopTracksByArtistName(@NotNull String query) {
        CacheAllocator cacheAllocator = cache.getAllocator("getTopTracksByArtistName", query, ArrayList.class);
        if (!cacheAllocator.exists()) {
            List<Track> toptracks = this.json.fromJson(retrieveReader("method=artist.gettoptracks&artist=".concat(URLEncoder.encode(query))), ArtistTopTracksSearchResult.class).getToptracks().getTrack();
            cacheAllocator.cacheObject(toptracks);
            return toptracks;
        }
        return (List<Track>) cacheAllocator.getObject();
    }

    @NotNull
    public List<Track> getTopTracksByArtistID(@NotNull String query) {
        CacheAllocator cacheAllocator = cache.getAllocator("getTopTracksByArtistID", query, ArrayList.class);
        if (!cacheAllocator.exists()) {
            List<Track> toptracks = this.json.fromJson(retrieveReader("method=artist.gettoptracks&mbid=".concat(query)), ArtistTopTracksSearchResult.class).getToptracks().getTrack();
            cacheAllocator.cacheObject(toptracks);
            return toptracks;
        }
        return (List<Track>) cacheAllocator.getObject();
    }

    @NotNull
    public List<Album> getTopAlbumsByArtistID(@NotNull String query) {
        return this.json
                .fromJson(
                        retrieveReader("method=artist.getTopAlbums&mbid="
                                .concat(query)),
                        TopAlbumSearchResult.class).getTopalbums().getAlbums();
    }

    @NotNull
    public List<Artist> SearchArtist(@NotNull String query) {
        query = query.toLowerCase();
        CacheAllocator cacheAllocator = cache.getAllocator("SearchArtist", query, ArrayList.class);

        if (!cacheAllocator.exists()) {
            List<Artist> searchresult = json
                    .fromJson(
                            retrieveReader("method=artist.search&limit=3&artist=".concat(java.net.URLEncoder
                                    .encode(query))), ArtistSearchResult.class)
                    .getResults().getArtistmatches().getArtist();
            cacheAllocator.cacheObject(searchresult);
            return searchresult;
        }

        return (List<Artist>) cacheAllocator.getObject();
    }

    @NotNull
    public List<Artist> getTopArtists() {
        CacheAllocator cacheAllocator = cache.getAllocator("getTopArtists", "", ArrayList.class);
        if (!cacheAllocator.exists()) {
            List<Artist> searchresult = json
                    .fromJson(
                            retrieveReader("method=chart.gettopartists&limit=10"), TopArtistsResult.class)
                    .getArtists().getArtist();
            cacheAllocator.cacheObject(searchresult);
            return searchresult;
        }
        return (List<Artist>) cacheAllocator.getObject();
    }

    @Nullable
    private Reader retrieveReader(@NotNull String query) {
        String apiKey = "1b726929f182175e22372a9a52ca76b0";
        String url = "http://ws.audioscrobbler.com/2.0/?api_key=".concat(apiKey).concat("&format=json&").concat(query);
        //System.out.println(url);

        InputStream source = WebWorker.retrieveStream(url);
        try {
            return new InputStreamReader(source, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }
}
