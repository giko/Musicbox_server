package com.musicbox;

import com.google.gson.Gson;

import java.util.HashMap;

public class Cache {
    private static HashMap<String, String> cache = new HashMap<String, String>();
    private static Gson json = new Gson();

    public static void cacheObject(int key, Object object) {
        cacheObject(String.valueOf(key), object);
    }

    public static void cacheObject(String key, Object object) {
        System.out.println(object.getClass().getName());
        cache.put(object.getClass().getName() + key, json.toJson(object));
    }

    public static Object getObject(int key, Class oclass) {
        return getObject(String.valueOf(key), oclass);
    }

    public static Object getObject(String key, Class oclass) {
        return json.fromJson(cache.get(oclass.getName() + key), oclass);
    }

    public static void setVariable(String key, String value) {
        cache.put(key, value);
    }

    public static String getVariable(String key) {
        return cache.get(key);
    }

    public static boolean exists(int key, Class oclass) {
        return exists(oclass.getName() + String.valueOf(key));
    }

    public static boolean exists(String key, Class oclass) {
        return exists(oclass.getName() + key);
    }

    public static boolean exists(String key) {
        return cache.containsKey(key);
    }
}
