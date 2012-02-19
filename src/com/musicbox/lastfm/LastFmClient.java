package com.musicbox.lastfm;

import com.google.gson.Gson;
import com.musicbox.Cache;
import com.musicbox.WebWorker;
import com.musicbox.lastfm.structure.artist.Album;
import com.musicbox.lastfm.structure.artist.Artist;
import com.musicbox.lastfm.structure.artist.ArtistSearchResult;
import com.musicbox.lastfm.structure.artist.TopAlbumSearchResult;
import com.musicbox.lastfm.structure.track.ArtistTopTracksSearchResult;
import com.musicbox.lastfm.structure.track.Track;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class LastFmClient {
    private final String ApiKey = "1b726929f182175e22372a9a52ca76b0";
    private final Gson json = new Gson();

    public List<Track> getTopTracksByArtistID(String query) {
        if (!Cache.exists(query, ArrayList.class)) {
            List<Track> toptracks = this.json.fromJson(retrieveReader("method=artist.gettoptracks&mbid=".concat(query)), ArtistTopTracksSearchResult.class).getToptracks().getTrack();
            Cache.cacheObject(query, toptracks);
            return toptracks;
        }
        return (List<Track>) Cache.getObject(query, ArrayList.class);
    }

    public List<Album> getTopAlbumsByArtistID(String query) {
        return this.json
                .fromJson(
                        retrieveReader("method=artist.getTopAlbums&mbid="
                                .concat(query)),
                        TopAlbumSearchResult.class).getTopalbums().getAlbums();
    }

    public List<Artist> SearchArtist(String query) {
        if (!Cache.exists(query, ArrayList.class)) {
            List<Artist> searchresult = json
                    .fromJson(
                            retrieveReader("method=artist.search&limit=3&artist=".concat(java.net.URLEncoder
                                    .encode(query))), ArtistSearchResult.class)
                    .getResults().getArtistmatches().getArtist();
            Cache.cacheObject(query, searchresult);
            return searchresult;
        }
        System.out.println("Arists loaded from cache");
        return (List<Artist>) Cache.getObject(query, ArrayList.class);
    }

    private Reader retrieveReader(String query) {
        String url = "http://ws.audioscrobbler.com/2.0/?api_key=".concat(ApiKey).concat("&format=json&").concat(query);
        //System.out.println(url);

        InputStream source = WebWorker.retrieveStream(url);
        return new InputStreamReader(source);
    }
}
