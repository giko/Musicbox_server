package com.musicbox.server.logic.weborama;

public class Genre {
	private Number canPlay;
	private String identifier;
	private String info;
	private String search;
	private String title;

	public Number getCanPlay() {
		return this.canPlay;
	}

	public void setCanPlay(Number canPlay) {
		this.canPlay = canPlay;
	}

	public String getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getInfo() {
		return this.info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getSearch() {
		return this.search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
