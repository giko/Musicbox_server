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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class LastFmClient {
    private final String ApiKey = "1b726929f182175e22372a9a52ca76b0";
    Type locationInfoListType = new TypeToken<List<Artist>>() {
    }.getType();
    Gson json = new GsonBuilder()
            .registerTypeAdapter(locationInfoListType, new ArtistTypeAdapter())
            .create();
    private static final Cache cache = new Cache();


    public List<Track> getTopTracksByArtistName(String query) {
        CacheAllocator cacheAllocator = cache.getAllocator("getTopTracksByArtistName", query, ArrayList.class);
        if (!cacheAllocator.exists()) {
            List<Track> toptracks = this.json.fromJson(retrieveReader("method=artist.gettoptracks&artist=".concat(URLEncoder.encode(query))), ArtistTopTracksSearchResult.class).getToptracks().getTrack();
            cacheAllocator.cacheObject(toptracks);
            return toptracks;
        }
        return (List<Track>) cacheAllocator.getObject();
    }

    public List<Track> getTopTracksByArtistID(String query) {
        CacheAllocator cacheAllocator = cache.getAllocator("getTopTracksByArtistID", query, ArrayList.class);
        if (!cacheAllocator.exists()) {
            List<Track> toptracks = this.json.fromJson(retrieveReader("method=artist.gettoptracks&mbid=".concat(query)), ArtistTopTracksSearchResult.class).getToptracks().getTrack();
            cacheAllocator.cacheObject(toptracks);
            return toptracks;
        }
        return (List<Track>) cacheAllocator.getObject();
    }

    public List<Album> getTopAlbumsByArtistID(String query) {
        return this.json
                .fromJson(
                        retrieveReader("method=artist.getTopAlbums&mbid="
                                .concat(query)),
                        TopAlbumSearchResult.class).getTopalbums().getAlbums();
    }

    public List<Artist> SearchArtist(String query) {
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

    private Reader retrieveReader(String query) {
        String url = "http://ws.audioscrobbler.com/2.0/?api_key=".concat(ApiKey).concat("&format=json&").concat(query);
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
