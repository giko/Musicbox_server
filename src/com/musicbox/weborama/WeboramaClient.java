package com.musicbox.weborama;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import com.google.gson.Gson;
import com.musicbox.server.logic.tools.MD5;
import com.musicbox.weborama.structure.Playlist;

public class WeboramaClient {
	SearchResult lastSearch = null;
	Playlist lastPlaylist = null;

	public SearchResult Search(String Query) {
		if (Query.length() > 10) {
			Query = Query.substring(0, 10);
		}

		String md5hash = MD5.getMD5(Query);
		String url = "http://www.weborama.ru/cache/search/helper/"
				.concat(md5hash.substring(0, 2)).concat("/").concat(md5hash)
				.concat(".json");

		InputStream source = retrieveStream(url);

		Gson gson = new Gson();

		Reader reader = new InputStreamReader(source);

		SearchResult response = gson.fromJson(reader, SearchResult.class);

		this.lastSearch = response;
		return response;
	}

	public Playlist GetArtistPlaylistByIdentifier(String identifier) {
		String url = "http://www.weborama.ru/modules/player/index_json.php?type=playlist&act=new&limit=10&filter=artistId:"
				.concat(identifier);

		InputStream source = retrieveStream(url);

		Gson gson = new Gson();

		Reader reader = new InputStreamReader(source);

		Playlist response = gson.fromJson(reader, Playlist.class);

		lastPlaylist = response;
		return response;
	}

	private InputStream retrieveStream(String url) {

		DefaultHttpClient client = new DefaultHttpClient();
		
		HttpGet getRequest = new HttpGet(url);

		try {

			HttpResponse getResponse = client.execute(getRequest);
			final int statusCode = getResponse.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK) {
				return null;
			}

			HttpEntity getResponseEntity = getResponse.getEntity();
			return getResponseEntity.getContent();

		} catch (IOException e) {
			getRequest.abort();
		}

		return null;

	}

	public SearchResult getLastSearch() {
		return lastSearch;
	}
}
