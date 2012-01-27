package com.musicbox.weborama.structure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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

	public List<Album> getAlbums(){
		List<Album> result = new ArrayList<Album>();
		
		try {
			Elements response = Jsoup.connect(info.concat("albums/")).get().select("div[id^=songList_lentaAlbums_]");
			System.out.println(response.html());
			for (Element albumElement : response){
				Album dummy = new Album();
				dummy.setInfo(albumElement.select("div[class^=cover]").select("a[href]").get(0).attr("href"));
				result.add(dummy);
			}
			
		} catch (IOException e) {
			result = null;
			e.printStackTrace();
		}
		
		return result;
	}
	
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
