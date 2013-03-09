package com.musicbox;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.musicbox.utils.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Cache {
    private static int totalcount = 0;
    private final HashMap<String, JsonElement> cache_ = new HashMap<String, JsonElement>();
    private final Gson json_ = new GsonBuilder().setExclusionStrategies(new BasicSerialisationExclusionStrategy()).create();
    private final CacheCleaner cleaner_ = new CacheCleaner(this);


    public Cache() {
        cleaner_.setDaemon(true);
        cleaner_.start();
    }

    @NotNull
    public CacheAllocator getAllocator(String method, String query, Class objclass) {
        return new CacheAllocator(this, method, query, objclass);
    }

    @NotNull
    public CacheAllocator getAllocator(String method, String query, Class objclass, int expirationTime) {
        return new CacheAllocator(this, method, query, objclass, expirationTime);
    }

    public void cacheObject(String method, String query, @NotNull Object object) {
        cache_.put(method + query + object.getClass().getName(), json_.toJsonTree(object));
        ++totalcount;
    }

    public void cacheObject(String method, String query, @NotNull Object object, int expirationTime) {
        cache_.put(method + query + object.getClass().getName(), json_.toJsonTree(object));
        cleaner_.add(method + query + object.getClass().getName(), expirationTime);
        ++totalcount;
    }

    public Object getObject(String method, String query, @NotNull Class objclass) {
        return json_.fromJson(cache_.get(method + query + objclass.getName()), objclass);
    }

    public void deleteObject(String method, String query, @NotNull Class objclass) {
        cache_.remove(method + query + objclass.getName());
    }

    public void deleteObject(String query) {
        cache_.remove(query);
    }

    public boolean exists(String method, String query, @NotNull Class objclass) {
        return cache_.containsKey(method + query + objclass.getName());
    }
}

class CacheCleaner extends Thread {
    private final Cache cache_;
    private final List<Pair<Integer, String>> list_ = new ArrayList<Pair<Integer, String>>();

    protected CacheCleaner(Cache cache) {
        cache_ = cache;
    }

    protected void add(String query, int time) {
        list_.add(new Pair<Integer, String>(time, query));
    }

    public void run() {
        while (true) {
            for (int i = 0; i < list_.size(); ++i) {
                Pair<Integer, String> element = list_.get(i);
                --element.first;
                if (element.first == 0) {
                    cache_.deleteObject(element.second);
                    list_.remove(element);
                    //System.out.println(element.second);
                }
            }

            try {
                this.sleep(1000 * 60 * 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }

}
