package com.musicbox.lastfm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.musicbox.*;
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
import com.musicbox.server.Config;
import com.musicbox.server.db.Connection;
import org.hibernate.Session;
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
    @NotNull
    private static final Cache cache = new Cache();
    final Type locationInfoListType = new TypeToken<List<Artist>>() {
    }.getType();
    final Type trackartisttype = new TypeToken<Artist>() {
    }.getType();
    final Type searchtracktype = new TypeToken<List<Track>>() {
    }.getType();
    final Type tagtype = new TypeToken<List<Tag>>() {
    }.getType();

    @NotNull
    public List<Artist> getSimilarArtistsByName(@NotNull String name) {
        @NotNull CacheAllocator cacheAllocator = cache.getAllocator("getSimilarArtistsByName", name, ArrayList.class);
        if (!cacheAllocator.exists()) {
            final Gson json = new GsonBuilder()
                    .registerTypeAdapter(locationInfoListType, new ArtistTypeAdapter())
                    .create();
            List<Artist> artists = json.fromJson(retrieveReader("method=artist.getsimilar&limit=10&artist=" + URLEncoder.encode(name)), SimilarArtistsResult.class).getSimilarartists().getArtist();

            cacheAllocator.cacheObject(artists);
            return artists;
        }
        return (List<Artist>) cacheAllocator.getObject();
    }

    @NotNull
    public Artist getArtistInfoByName(@NotNull String name) {
        @NotNull CacheAllocator cacheAllocator = cache.getAllocator("getArtistInfoByName", name, Artist.class);
        if (!cacheAllocator.exists()) {
            final Gson json = new GsonBuilder()
                    .registerTypeAdapter(locationInfoListType, new ArtistTypeAdapter())
                    .create();
            Artist artist = json.fromJson(retrieveReader("method=artist.getinfo&artist=" + URLEncoder.encode(name)), Artistmatches.class).getArtist().get(0);

            Bio artistBio = artist.getBio();
            //artistBio.setContent(artistBio.getContent().replaceAll("\\<.*?>", ""));
            artistBio.setSummary(artistBio.getSummary().replaceAll("\\<.*?>", ""));

            cacheAllocator.cacheObject(artist);
            return artist;
        }
        return (Artist) cacheAllocator.getObject();
    }

    @NotNull
    public Artist getArtistInfoById(@NotNull String id) {
        @NotNull CacheAllocator cacheAllocator = cache.getAllocator("getArtistInfoById", id, Artist.class);
        if (!cacheAllocator.exists()) {
            final Gson json = new GsonBuilder()
                    .registerTypeAdapter(locationInfoListType, new ArtistTypeAdapter())
                    .create();
            Artist artist = json.fromJson(retrieveReader("method=artist.getinfo&mbid=" + URLEncoder.encode(id)), Artistmatches.class).getArtist().get(0);

            Bio artistBio = artist.getBio();
            //artistBio.setContent(artistBio.getContent().replaceAll("\\<.*?>", ""));
            artistBio.setSummary(artistBio.getSummary().replaceAll("\\<.*?>", ""));

            cacheAllocator.cacheObject(artist);

            return artist;
        }
        return (Artist) cacheAllocator.getObject();
    }

    @NotNull
    public List<Track> getTopTracksByArtistName(@NotNull String query) {
        @NotNull CacheAllocator cacheAllocator = cache.getAllocator("getTopTracksByArtistName", query, ArrayList.class, 24);
        if (!cacheAllocator.exists()) {
            final Gson json = new GsonBuilder()
                    .registerTypeAdapter(locationInfoListType, new ArtistTypeAdapter())
                    .create();
            List<Track> toptracks = json.fromJson(retrieveReader("method=artist.gettoptracks&artist=" + URLEncoder.encode(query)), ArtistTopTracksSearchResult.class).getToptracks().getTrack();
            cacheAllocator.cacheObject(toptracks);
            return toptracks;
        }
        return (List<Track>) cacheAllocator.getObject();
    }

    @NotNull
    public List<Track> getTopTracksByArtistID(@NotNull String query) {
        @NotNull CacheAllocator cacheAllocator = cache.getAllocator("getTopTracksByArtistID", query, ArrayList.class, 24);
        if (!cacheAllocator.exists()) {
            final Gson json = new GsonBuilder()
                    .registerTypeAdapter(locationInfoListType, new ArtistTypeAdapter())
                    .create();
            List<Track> toptracks = json.fromJson(retrieveReader("method=artist.gettoptracks&mbid=" + query), ArtistTopTracksSearchResult.class).getToptracks().getTrack();
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
                        retrieveReader("method=artist.getTopAlbums&mbid=" + query),
                        TopAlbumSearchResult.class).getTopalbums().getAlbums();
    }
    //final Type trackmatchestype = new TypeToken<Trackmatches>() {
    //}.getType();

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
                                retrieveReader("method=artist.search&limit=3&artist=" + URLEncoder
                                        .encode(query)), ArtistSearchResult.class)
                        .getResults().getArtistmatches().getArtist();
                cacheAllocator.cacheObject(searchresult);
                return searchresult;
            } catch (Exception e) {
                if (Config.getInstance().isLastfmshowdebugginginfo()) {
                    e.printStackTrace();
                }
                return null;
            }
        }

        return (List<Artist>) cacheAllocator.getObject();
    }

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
                        .fromJson(retrieveReader("method=track.search&limit=10&track=" + URLEncoder.encode(query)), TrackSearch.class)
                        .getResults().getTrackmatches().getTrack();
                cacheAllocator.cacheObject(searchresult);
                return searchresult;
            } catch (Exception e) {
                if (Config.getInstance().isLastfmshowdebugginginfo()) {
                    e.printStackTrace();
                }
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
                if (Config.getInstance().isLastfmshowdebugginginfo()) {
                    e.printStackTrace();
                }
                return null;
            }
        }
        return (List<Tag>) cacheAllocator.getObject();
    }

    @NotNull
    public List<Artist> getTopArtists() {
        @NotNull CacheAllocator cacheAllocator = cache.getAllocator("getTopArtists", "", ArrayList.class, 12);
        if (!cacheAllocator.exists()) {
            final Gson json = new GsonBuilder()
                    .setExclusionStrategies(new BasicSerialisationExclusionStrategy())
                    .registerTypeAdapter(locationInfoListType, new ArtistTypeAdapter())
                    .create();
            List<Artist> searchresult = json
                    .fromJson(
                            retrieveReader("method=chart.gettopartists&limit=10"), TopArtistsResult.class)
                    .getArtists().getArtist();

            Session session = Connection.getSession();

            for (Artist artist : searchresult) {
                artist.setData();
                session.saveOrUpdate(artist);
            }
            session.flush();
            session.close();

            return searchresult;
        }
        return (List<Artist>) cacheAllocator.getObject();
    }

    @Nullable
    private Reader retrieveReader(@NotNull String query) {
        String url = (("http://ws.audioscrobbler.com/2.0/?api_key=" + Config.getInstance().getLastfmapikey()) + "&format=json&") + query;
        if (Config.getInstance().isLastfmshowdebugginginfo()) {
            System.out.println(url);
        }

        @Nullable InputStream source = WebWorker.retrieveStream(url);
        try {
            return new InputStreamReader(source, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
