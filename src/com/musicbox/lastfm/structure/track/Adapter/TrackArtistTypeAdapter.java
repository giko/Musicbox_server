package com.musicbox.lastfm.structure.track.Adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.musicbox.lastfm.structure.artist.Artist;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

/**
 * Created by IntelliJ IDEA.
 * User: giko
 * Date: 23.02.12
 * Time: 13:44
 */
public class TrackArtistTypeAdapter implements JsonDeserializer<Artist> {

    @NotNull
    @Override
    public Artist deserialize(@NotNull JsonElement jsonElement, Type type, @NotNull JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        @NotNull Artist val = new Artist();
        if (jsonElement.isJsonObject()) {
            val = (Artist) jsonDeserializationContext.deserialize(jsonElement, Artist.class);
        } else if (!jsonElement.isJsonObject()) {
            val.setName((String) jsonDeserializationContext.deserialize(jsonElement, String.class));
        } else {
            throw new RuntimeException("Unexpected JSON type: " + jsonElement.getClass());
        }
        return val;
    }
}