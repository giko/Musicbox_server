package com.musicbox.lastfm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.musicbox.Cache;
import com.musicbox.CacheAllocator;
import com.musicbox.WebWorker;
import com.musicbox.lastfm.structure.SingleArrayListType;
import com.musicbox.lastfm.structure.artist.Adapter.ArtistTypeAdapter;
import com.musicbox.lastfm.structure.artist.*;
import com.musicbox.lastfm.structure.tag.Tag;
import com.musicbox.lastfm.structure.tag.TagSearchResult;
import com.musicbox.lastfm.structure.tag.TopArtistSearch;
import com.musicbox.lastfm.structure.track.Adapter.TrackArtistTypeAdapter;
import com.musicbox.lastfm.structure.track.ArtistTopTracksSearchResult;
import com.musicbox.lastfm.structure.track.Track;
import com.musicbox.lastfm.structure.track.TrackSearch;
import com.musicbox.lastfm.structure.track.Trackmatches;
import com.musicbox.server.Config;
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

    @NotNull
    private static final Cache cache = new Cache();

    @NotNull
    public List<Track> getTopTracksByArtistName(@NotNull String query) {
        @NotNull CacheAllocator cacheAllocator = cache.getAllocator("getTopTracksByArtistName", query, ArrayList.class);
        if (!cacheAllocator.exists()) {
            final Gson json = new GsonBuilder()
                    .registerTypeAdapter(locationInfoListType, new ArtistTypeAdapter())
                    .create();
            List<Track> toptracks = json.fromJson(retrieveReader("method=artist.gettoptracks&artist=".concat(URLEncoder.encode(query))), ArtistTopTracksSearchResult.class).getToptracks().getTrack();
            cacheAllocator.cacheObject(toptracks);
            return toptracks;
        }
        return (List<Track>) cacheAllocator.getObject();
    }

    @NotNull
    public List<Track> getTopTracksByArtistID(@NotNull String query) {
        @NotNull CacheAllocator cacheAllocator = cache.getAllocator("getTopTracksByArtistID", query, ArrayList.class);
        if (!cacheAllocator.exists()) {
            final Gson json = new GsonBuilder()
                    .registerTypeAdapter(locationInfoListType, new ArtistTypeAdapter())
                    .create();
            List<Track> toptracks = json.fromJson(retrieveReader("method=artist.gettoptracks&mbid=".concat(query)), ArtistTopTracksSearchResult.class).getToptracks().getTrack();
            cacheAllocator.cacheObject(toptracks);
            return toptracks;
        }
        return (List<Track>) cacheAllocator.getObject();
    }

    @NotNull
    public List<Album> getTopAlbumsByArtistID(@NotNull String query) {
        final Gson json = new GsonBuilder()
                .registerTypeAdapter(locationInfoListType, new ArtistTypeAdapter())
                .create();
        return json
                .fromJson(
                        retrieveReader("method=artist.getTopAlbums&mbid="
                                .concat(query)),
                        TopAlbumSearchResult.class).getTopalbums().getAlbums();
    }

    @Nullable
    public List<Artist> SearchArtist(@NotNull String query) {
        query = query.toLowerCase();
        @NotNull CacheAllocator cacheAllocator = cache.getAllocator("SearchArtist", query, ArrayList.class);

        if (!cacheAllocator.exists()) {
            final Gson json = new GsonBuilder()
                    .registerTypeAdapter(locationInfoListType, new SingleArrayListType<Artist>(Artist.class))
                    .create();
            try {
                List<Artist> searchresult = json
                        .fromJson(
                                retrieveReader("method=artist.search&limit=3&artist=".concat(java.net.URLEncoder
                                        .encode(query))), ArtistSearchResult.class)
                        .getResults().getArtistmatches().getArtist();
                cacheAllocator.cacheObject(searchresult);
                return searchresult;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        return (List<Artist>) cacheAllocator.getObject();
    }

    final Type trackartisttype = new TypeToken<Artist>() {
    }.getType();
    final Type searchtracktype = new TypeToken<List<Track>>() {
    }.getType();
    final Type trackmatchestype = new TypeToken<Trackmatches>() {
    }.getType();

    @Nullable
    public List<Track> SearchTrack(@NotNull String query) {
        query = query.toLowerCase();
        @NotNull CacheAllocator cacheAllocator = cache.getAllocator("SearchTrack", query, ArrayList.class);
        if (!cacheAllocator.exists()) {
            final Gson jsontracks = new GsonBuilder()
                    .registerTypeAdapter(searchtracktype, new SingleArrayListType<Track>(Track.class))
                    .registerTypeAdapter(trackartisttype, new TrackArtistTypeAdapter())
                            //.registerTypeAdapter(trackmatchestype, new TrackMatchesType())
                    .create();
            try {
                List<Track> searchresult = jsontracks
                        .fromJson(retrieveReader("method=track.search&limit=3&track=".concat(URLEncoder.encode(query))), TrackSearch.class)
                        .getResults().getTrackmatches().getTrack();
                cacheAllocator.cacheObject(searchresult);
                return searchresult;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        return (List<Track>) cacheAllocator.getObject();
    }

    @NotNull
    public List<Artist> SearchArtistByTag(String query) {
        query = URLEncoder.encode(query.toLowerCase().trim());
        @NotNull CacheAllocator cacheAllocator = cache.getAllocator("SearchArtistByTag", query, ArrayList.class);
        if (!cacheAllocator.exists()) {
            @NotNull final Gson json = new Gson();
            List<Artist> searchresult = json.fromJson(retrieveReader("method=tag.gettopartists&limit=10&tag=" + query), TopArtistSearch.class)
                    .getTopartists().getArtist();
            cacheAllocator.cacheObject(searchresult);
            return searchresult;
        }

        return (List<Artist>) cacheAllocator.getObject();
    }


    final Type tagtype = new TypeToken<List<Tag>>() {
    }.getType();

    @Nullable
    public List<Tag> SearchTag(String query) {
        query = URLEncoder.encode(query.toLowerCase().trim());
        @NotNull CacheAllocator cacheAllocator = cache.getAllocator("SearchTag", query, ArrayList.class);
        if (!cacheAllocator.exists()) {
            final Gson json = new GsonBuilder()
                    .registerTypeAdapter(tagtype, new SingleArrayListType<Tag>(Tag.class))
                    .create();
            try {
                List<Tag> searchresult = json.fromJson(retrieveReader("method=tag.search&limit=3&tag=" + query), TagSearchResult.class)
                        .getResults().getTagmatches().getTags();
                cacheAllocator.cacheObject(searchresult);
                return searchresult;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return (List<Tag>) cacheAllocator.getObject();
    }

    @NotNull
    public List<Artist> getTopArtists() {
        @NotNull CacheAllocator cacheAllocator = cache.getAllocator("getTopArtists", "", ArrayList.class);
        if (!cacheAllocator.exists()) {
            final Gson json = new GsonBuilder()
                    .registerTypeAdapter(locationInfoListType, new ArtistTypeAdapter())
                    .create();
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
        String url = "http://ws.audioscrobbler.com/2.0/?api_key=".concat(Config.getInstance().getLastfmapikey()).concat("&format=json&").concat(query);
        System.out.println(url);

        @Nullable InputStream source = WebWorker.retrieveStream(url);
        try {
            return new InputStreamReader(source, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }
}
