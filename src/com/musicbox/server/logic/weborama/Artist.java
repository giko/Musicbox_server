package com.musicbox.server.logic.weborama;

public class Artist {
	private Number canPlay;
	private Number category;
	private String identifier;
	private String image;
	private String info;
	private String search;
	private String title;
	private String titleRu;

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

	public String getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
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

	public String getTitleRu() {
		return this.titleRu;
	}

	public void setTitleRu(String titleRu) {
		this.titleRu = titleRu;
	}
}
