package com.musicbox.lastfm.structure.tag;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Giko
 * Date: 24.02.12
 * Time: 10:18
 * To change this template use File | Settings | File Templates.
 */
public class TagMatches {
    @SerializedName("tag")
    List<Tag> tags;

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
