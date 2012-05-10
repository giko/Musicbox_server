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
    private final Cache cache_;
    private final Class objClass_;
    private final String method_;
    private final String query_;
    private final int expirationTime_;

    public CacheAllocator(Cache cache, String method, String query, Class objclass) {
        cache_ = cache;
        method_ = method;
        query_ = query;
        objClass_ = objclass;
        expirationTime_ = 0;
    }

    public CacheAllocator(Cache cache, String method, String query, Class objclass, int expirationTime) {
        cache_ = cache;
        method_ = method;
        query_ = query;
        objClass_ = objclass;
        expirationTime_ = expirationTime;
    }

    public boolean exists() {
        return cache_.exists(method_, query_, objClass_);
    }

    public Object getObject() {
        return cache_.getObject(method_, query_, objClass_);
    }

    public void cacheObject(@NotNull Object object) {
        if (expirationTime_ == 0) {
            cache_.cacheObject(method_, query_, object);
        } else {
            cache_.cacheObject(method_, query_, object, expirationTime_);
        }
    }
}
