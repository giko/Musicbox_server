package com.musicbox.weborama;

import java.util.List;

import com.musicbox.weborama.structure.Album;
import com.musicbox.weborama.structure.Artist;
import com.musicbox.weborama.structure.Genre;
import com.musicbox.weborama.structure.Song;

public class SearchResult {
	private List<Album> album;
	private List<Artist> artist;
	private int errorCode;
	private List<Genre> genre;
	private List<Song> song;
	
	public List<Album> getAlbums() {
		return album;
	}
	public List<Artist> getArtists() {
		return artist;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public List<Genre> getGenres() {
		return genre;
	}
	public List<Song> getSongs() {
		return song;
	}
}
