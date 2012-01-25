package com.musicbox.server.logic.weborama;

public class Song {
	private Number canPlay;
	private Number category;
	private String creator;
	private String creatorRu;
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

	public Number getCategory() {
		return this.category;
	}

	public void setCategory(Number category) {
		this.category = category;
	}

	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreatorRu() {
		return this.creatorRu;
	}

	public void setCreatorRu(String creatorRu) {
		this.creatorRu = creatorRu;
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
