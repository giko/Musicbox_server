package com.musicbox.vkontakte;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import com.google.gson.Gson;
import com.musicbox.vkontakte.structure.profiles.Profile;
import com.musicbox.vkontakte.structure.profiles.ProfileSearch;
import com.musicbox.WebWorker;

public class VkontakteClient {
	public Profile getProfileById(int id, String token) {
		Gson json = new Gson();
		return json
				.fromJson(
						retrieveReader(
								"getProfiles?uids="
										.concat(String.valueOf(id))
										.concat("&fields=photo_big,sex,bdate,city,country"),
								token), ProfileSearch.class).getResponse()
				.get(0);
	}

	private Reader retrieveReader(String query, String token) {
		String url = "https://api.vkontakte.ru/method/".concat(query)
				.concat("&access_token=").concat(token);
System.out.println(url);
		InputStream source = WebWorker.retrieveStream(url);
		try {
			return new InputStreamReader(source, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
