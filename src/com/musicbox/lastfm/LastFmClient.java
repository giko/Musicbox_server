package com.musicbox.lastfm;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

import com.musicbox.lastfm.structure.artist.*;
import com.musicbox.WebWorker;

public class LastFmClient {
	private String ApiKey = "1b726929f182175e22372a9a52ca76b0";

	public List<Album> GetTopAlbumsByArtistID(String query) {
		Gson json = new Gson();

		return json
				.fromJson(
						retrieveReader("method=artist.getTopAlbums&mbid="
								.concat(query)),
						TopAlbumSearchResult.class).getTopalbums().getAlbums();
	}

	public List<Artist> SearchArtist(String query) {
		Gson json = new Gson();

		return json
				.fromJson(
						retrieveReader("method=artist.search&artist=".concat(java.net.URLEncoder
								.encode(query))), ArtistSearchResult.class)
				.getResults().getArtistmatches().getArtist();
	}

	private Reader retrieveReader(String query){
		String url = "http://ws.audioscrobbler.com/2.0/?api_key=".concat(ApiKey).concat("&format=json&").concat(query);

		InputStream source = WebWorker.retrieveStream(url);
		return  new InputStreamReader(source);
	}
}
