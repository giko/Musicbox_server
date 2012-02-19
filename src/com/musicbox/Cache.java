package com.musicbox;

import com.google.gson.Gson;

import java.util.HashMap;

public class Cache {
    private HashMap<String, String> cache = new HashMap<String, String>();
    private Gson json = new Gson();

    public CacheAllocator getAllocator(String method, String query, Class objclass) {
        return new CacheAllocator(this, method, query, objclass);
    }

    public void cacheObject(String method, String query, Object object) {
        cache.put(method + query + object.getClass().getName(), json.toJson(object));
    }

    public Object getObject(String method, String query, Class objclass) {
        return json.fromJson(cache.get(method + query + objclass.getName()), objclass);
    }

    public String getObjectData(String method, String query, Class objclass) {
        return cache.get(method + query + objclass.getName());
    }

    public boolean exists(String method, String query, Class objclass) {
        return cache.containsKey(method + query + objclass.getName());
    }
}
