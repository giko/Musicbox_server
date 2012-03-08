package com.musicbox;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;

public class WebWorker {
    @Nullable
    public static InputStream retrieveStream(String url) {

        @NotNull DefaultHttpClient client = new DefaultHttpClient();

        @NotNull HttpGet getRequest = new HttpGet(url);

        try {

            HttpResponse getResponse = client.execute(getRequest);
            final int statusCode = getResponse.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }

            HttpEntity getResponseEntity = getResponse.getEntity();
            return getResponseEntity.getContent();

        } catch (IOException e) {
            getRequest.abort();
        }

        return null;

    }
}
