package com.musicbox.weborama;

import com.google.gson.Gson;
import com.musicbox.WebWorker;
import com.musicbox.server.logic.tools.MD5;
import com.musicbox.weborama.structure.Playlist;
import com.musicbox.weborama.structure.SearchResult;
import com.musicbox.weborama.structure.TrackList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class WeboramaClient {
    @Nullable
    SearchResult lastSearch = null;
    @Nullable
    Playlist lastPlaylist = null;

    public SearchResult Search(@NotNull String Query) {
        if (Query.length() > 10) {
            Query = Query.substring(0, 10);
        }

        Query = Query.toLowerCase();

        String md5hash = MD5.getMD5(Query);
        String url = "http://www.weborama.ru/cache/search/helper/"
                .concat(md5hash.substring(0, 2)).concat("/").concat(md5hash)
                .concat(".json");

        @Nullable InputStream source = WebWorker.retrieveStream(url);

        @NotNull Gson gson = new Gson();

        @NotNull Reader reader = new InputStreamReader(source);

        SearchResult response = gson.fromJson(reader, SearchResult.class);

        this.lastSearch = response;
        return response;
    }

    public Playlist GetArtistPlaylistByIdentifier(String identifier) {
        String url = "http://www.weborama.ru/modules/player/index_json.php?type=playlist&act=new&limit=10&filter=artistId:"
                .concat(identifier).concat(";air:0");

        @Nullable InputStream source = WebWorker.retrieveStream(url);

        @NotNull Gson gson = new Gson();

        @NotNull Reader reader = new InputStreamReader(source);

        Playlist response = gson.fromJson(reader, Playlist.class);

        lastPlaylist = response;
        return response;
    }

    public Playlist GetAlbumPlaylistByIdentifier(String identifier) {
        String url = "http://www.weborama.ru/modules/player/index_json.php?id=".concat(identifier).concat("&type=album&act=new&limit=10");

        @Nullable InputStream source = WebWorker.retrieveStream(url);

        @NotNull Gson gson = new Gson();

        @NotNull Reader reader = new InputStreamReader(source);

        Playlist response = gson.fromJson(reader, Playlist.class);

        lastPlaylist = response;
        return response;
    }

    public TrackList GetTrackBySongIdentifier(String identifier) {
        String url = "http://www.weborama.ru/modules/player/index_json.php?id=".concat(identifier).concat("&type=audio&act=new&mood=3&limit=0");

        @Nullable InputStream source = WebWorker.retrieveStream(url);

        @NotNull Gson gson = new Gson();

        @NotNull Reader reader = new InputStreamReader(source);

        Playlist response = gson.fromJson(reader, Playlist.class);

        return response.getTrackList().get(0);
    }

    @Nullable
    public SearchResult getLastSearch() {
        return lastSearch;
    }
}
