package com.musicbox;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * Created with IntelliJ IDEA.
 * User: giko
 * Date: 3/8/13
 * Time: 9:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class BasicSerialisationExclusionStrategy implements
        ExclusionStrategy {

    public boolean shouldSkipField(FieldAttributes fa) {
        return fa.getAnnotation(ExcludeFromSerialisation.class) !=
                null;
    }

    public boolean shouldSkipClass(Class<?> type) {
        return false;
    }
}


