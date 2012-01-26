package com.musicbox.weborama.structure;

public class Album {
	private byte canPlay;
	private byte category;
	private String creator;
	private String creatorRu;
	private String identifier;
	private String image;
	private String info;
	private String search;
	private String title;

	public Number getCanPlay() {
		return canPlay;
	}

	public Number getCategory() {
		return category;
	}

	public String getCreator() {
		return creator;
	}

	public String getCreatorRu() {
		return creatorRu;
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
}
