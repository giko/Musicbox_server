package com.musicbox.model.lastfm.structure.artist.Adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.musicbox.model.lastfm.structure.artist.Artist;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: giko
 * Date: 19.02.12
 * Time: 14:34
 */
public class ArtistTypeAdapter implements JsonDeserializer<List<Artist>> {


    @NotNull
    @Override
    public List<Artist> deserialize(@NotNull JsonElement jsonElement, Type type, @NotNull JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        @NotNull List<Artist> vals = new ArrayList<Artist>();
        if (jsonElement.isJsonArray()) {
            for (JsonElement e : jsonElement.getAsJsonArray()) {
                vals.add((Artist) jsonDeserializationContext.deserialize(e, Artist.class));
            }
        } else if (jsonElement.isJsonObject()) {
            vals.add((Artist) jsonDeserializationContext.deserialize(jsonElement, Artist.class));
        } else {
            throw new RuntimeException("Unexpected JSON type: " + jsonElement.getClass());
        }
        return vals;
    }

}
