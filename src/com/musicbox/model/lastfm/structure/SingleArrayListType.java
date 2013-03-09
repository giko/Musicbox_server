package com.musicbox.model.lastfm.structure;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: giko
 * Date: 08.03.12
 * Time: 10:58
 */
public class SingleArrayListType<T> implements JsonDeserializer<List<T>> {
    @NotNull
    private Class deserializationclass;

    public SingleArrayListType(@NotNull Class dClass) {
        this.deserializationclass = dClass;
    }

    @NotNull
    @Override
    public List<T> deserialize(@NotNull JsonElement jsonElement, Type type, @NotNull JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        @NotNull List<T> vals = new ArrayList<T>();
        if (jsonElement.isJsonArray()) {
            for (JsonElement e : jsonElement.getAsJsonArray()) {
                vals.add((T) jsonDeserializationContext.deserialize(e, deserializationclass));
            }
        } else if (jsonElement.isJsonObject()) {
            vals.add((T) jsonDeserializationContext.deserialize(jsonElement, deserializationclass));
        } else {
            throw new RuntimeException("Unexpected JSON type: " + jsonElement.getClass());
        }
        return vals;
    }
}
