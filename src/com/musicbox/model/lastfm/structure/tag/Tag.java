package com.musicbox.model.lastfm.structure.tag;

/**
 * Created by IntelliJ IDEA.
 * User: Giko
 * Date: 24.02.12
 * Time: 10:07
 * To change this template use File | Settings | File Templates.
 */
public class Tag {
    String name;
    int count;
    String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
