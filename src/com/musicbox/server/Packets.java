package com.musicbox.server;

import java.util.List;

import com.musicbox.weborama.structure.SearchResult;
import com.musicbox.weborama.structure.TrackList;

public class Packets {
	static public class Incoming {
		public enum Action {
			SEARCH, LISTENING, LOGINBYTOKEN, LOGINBYCODE, GETSONGBYID
		}

		private Action action;
		private String loginUsername;
		private String message;
		
		
		public Action getAction() {
			return action;
		}
		public void setAction(Action action) {
			this.action = action;
		}
		public String getLoginUsername() {
			return loginUsername;
		}
		public void setLoginUsername(String loginUsername) {
			this.loginUsername = loginUsername;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
	}

	static public class Outgoing {
		public enum Action {
			LISTENING, SEARCHRESULT, JOIN, LEAVE, SONGS, TOKEN, MESSAGE
		}
		
		public Outgoing(Action caction){
			this.action = caction;
		}
		
		public Outgoing() {
			// TODO Auto-generated constructor stub
		}

		private Action action;
		private SearchResult result;
		private String message;
		private String username;
		private List<TrackList> songs;
		
		public Action getAction() {
			return action;
		}
		public void setAction(Action action) {
			this.action = action;
		}
		public SearchResult getResult() {
			return result;
		}
		public void setResult(SearchResult result) {
			this.result = result;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public List<TrackList> getSongs() {
			return songs;
		}
		public void setSongs(List<TrackList> songs) {
			this.songs = songs;
		}
	}
}
