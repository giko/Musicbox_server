package com.musicbox.weborama.structure;

public class TrackList {
	private String album;
	private int bitrate;
	private String creator;
	private int duration;
	private int frequency;
	private String identifier;
	private Image image;
	private String info;
	private String location;
	private String mimeType;
	private byte mood;
	private boolean random;
	private int rating;
	private String title;
	private byte trackNum;

	public String getAlbum() {
		return album;
	}

	public int getBitrate() {
		return bitrate;
	}

	public String getCreator() {
		return creator;
	}

	public int getDuration() {
		return duration;
	}

	public int getFrequency() {
		return frequency;
	}

	public String getIdentifier() {
		return identifier;
	}

	public Image getImage() {
		return image;
	}

	public String getInfo() {
		return info;
	}

	public String getLocation() {
		return location;
	}

	public String getMimeType() {
		return mimeType;
	}

	public int getMood() {
		return mood;
	}

	public boolean isRandom() {
		return random;
	}

	public int getRating() {
		return rating;
	}

	public String getTitle() {
		return title;
	}

	public int getTrackNum() {
		return trackNum;
	}
}
