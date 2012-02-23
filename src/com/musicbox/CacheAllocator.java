package com.musicbox;

import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: giko
 * Date: 19.02.12
 * Time: 11:28
 * To change this template use File | Settings | File Templates.
 */
public class CacheAllocator {
    private final Cache cache;
    private final Class objclass;
    private final String method;
    private final String query;

    public CacheAllocator(Cache cache, String method, String query, Class objclass) {
        this.cache = cache;
        this.method = method;
        this.query = query;
        this.objclass = objclass;
    }

    public boolean exists() {
        return cache.exists(this.method, this.query, this.objclass);
    }

    public Object getObject() {
        return cache.getObject(this.method, this.query, this.objclass);
    }

    public void cacheObject(@NotNull Object object) {
        cache.cacheObject(this.method, this.query, object);
    }

    //public String getObjectData() {
    //   return cache.getObjectData(this.method, this.query, this.objclass);
    //}
}