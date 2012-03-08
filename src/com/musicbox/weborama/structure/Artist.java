package com.musicbox.weborama.structure;

import com.musicbox.weborama.WeboramaClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Artist {
    private byte canPlay;
    private byte category;
    private String identifier;
    private String image;
    private String info;
    private String search;
    private String title;
    private String titleRu;

    @Nullable
    public List<Album> getAlbums() {
        @Nullable List<Album> result = new ArrayList<Album>();

        try {
            Elements response = Jsoup.connect(info.concat("albums/")).get().select("div[id^=songList_lentaAlbums_]");
            for (@NotNull Element albumElement : response) {
                @NotNull Album dummy = new Album();
                dummy.setCanPlay(canPlay);
                dummy.setCategory(category);
                dummy.setCreator(title);
                dummy.setCreatorRu(titleRu);
                dummy.setImage(albumElement.select("img[width=150]").get(0).attr("src"));
                dummy.setTitle(albumElement.select("a[id^=link_album_").get(0).text());
                dummy.setIdentifier(albumElement.select("a[id^=link_album_").get(0).attr("id").replace("link_album_", ""));
                dummy.setInfo(albumElement.select("div[class^=cover]").select("a[href]").get(0).attr("href"));
                result.add(dummy);
            }
        } catch (IOException e) {
            result = null;
            e.printStackTrace();
        }

        return result;
    }

    public List<TrackList> getTracks() {
        @NotNull WeboramaClient client = new WeboramaClient();
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
