package com.musicbox.lastfm.structure.artist;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Topalbums {
	@SerializedName("album")
	private List<Album> albums;

	public List<Album> getAlbums() {
		return albums;
	}

	public void setAlbums(List<Album> albums) {
		this.albums = albums;
	}

}
