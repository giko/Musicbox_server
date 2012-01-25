package com.musicbox.server.logic.weborama;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import com.google.gson.Gson;
import com.musicbox.server.logic.tools.MD5;

public class WeboramaClient {
	SearchResult last_search = null;
	
	public SearchResult Search(String Query){
		String md5hash = MD5.getMD5(Query);
		String url = "http://www.weborama.ru/cache/search/helper/".concat(md5hash.substring(0, 2)).concat("/").concat(md5hash).concat(".json");
		
        InputStream source = retrieveStream(url);
        
        Gson gson = new Gson();
        
        Reader reader = new InputStreamReader(source);
        
        SearchResult response = gson.fromJson(reader, SearchResult.class);
        
        this.last_search = response;
        return response;
	}
	
    private InputStream retrieveStream(String url) {
        
        DefaultHttpClient client = new DefaultHttpClient(); 
        
        HttpGet getRequest = new HttpGet(url);
          
        try {
           
           HttpResponse getResponse = client.execute(getRequest);
           final int statusCode = getResponse.getStatusLine().getStatusCode();
           
           if (statusCode != HttpStatus.SC_OK) { 
              return null;
           }

           HttpEntity getResponseEntity = getResponse.getEntity();
           return getResponseEntity.getContent();
           
        } 
        catch (IOException e) {
           getRequest.abort();
        }
        
        return null;
        
     }

	public SearchResult getLastSearch() {
		return last_search;
	}
}
