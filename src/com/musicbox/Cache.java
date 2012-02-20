package com.musicbox;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Cache {
    @NotNull
    private HashMap<String, JsonElement> cache = new HashMap<String, JsonElement>();
    @NotNull
    private Gson json = new Gson();
    private static int totalcount = 0;

    @NotNull
    public CacheAllocator getAllocator(String method, String query, Class objclass) {
        return new CacheAllocator(this, method, query, objclass);
    }

    public void cacheObject(String method, String query, @NotNull Object object) {
        cache.put(method + query + object.getClass().getName(), json.toJsonTree(object));
        ++totalcount;
    }

    public Object getObject(String method, String query, @NotNull Class objclass) {
        return json.fromJson(cache.get(method + query + objclass.getName()), objclass);
    }

    //public String getObjectData(String method, String query, Class objclass) {
    //    return cache.get(method + query + objclass.getName());
    //}

    public boolean exists(String method, String query, @NotNull Class objclass) {
        return cache.containsKey(method + query + objclass.getName());
    }
}
