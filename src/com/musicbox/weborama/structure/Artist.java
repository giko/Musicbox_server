package com.musicbox.weborama.structure;

import java.util.List;

import com.musicbox.weborama.WeboramaClient;

public class Artist {
	private byte canPlay;
	private byte category;
	private String identifier;
	private String image;
	private String info;
	private String search;
	private String title;
	private String titleRu;

	public List<TrackList> getTracks(){
		WeboramaClient client = new WeboramaClient();
		return client.GetArtistPlaylistByIdentifier(identifier).getTrackList();
	}
	
	public Number getCanPlay() {
		return canPlay;
	}

	public Number getCategory() {
		return category;
	}

	public String getIdentifier() {
		return identifier;
	}

	public String getImage() {
		return image;
	}

	public String getInfo() {
		return info;
	}

	public String getSearch() {
		return search;
	}

	public String getTitle() {
		return title;
	}

	public String getTitleRu() {
		return titleRu;
	}

}
