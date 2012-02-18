package com.musicbox.weborama.structure;

import com.musicbox.weborama.WeboramaClient;

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

    public Playlist getPlaylist() {
        WeboramaClient client = new WeboramaClient();

        return client.GetAlbumPlaylistByIdentifier(identifier);
    }

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

    public void setCanPlay(byte canPlay) {
        this.canPlay = canPlay;
    }

    public void setCategory(byte category) {
        this.category = category;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setCreatorRu(String creatorRu) {
        this.creatorRu = creatorRu;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
