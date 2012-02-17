package com.musicbox.lastfm.structure.artist;

import com.google.gson.annotations.SerializedName;

public class Image {
	@SerializedName("#text")
	private String link;
	private String size;
	
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}

}
