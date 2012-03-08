package com.musicbox.server;

import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: giko
 * Date: 05.03.12
 * Time: 22:00
 */
public class Config {
    @NotNull
    private static Config ourInstance = new Config();

    private String vkappid;
    private String vksecretkey;
    private String lastfmapikey;

    public String getLastfmapikey() {
        return lastfmapikey;
    }

    public void setLastfmapikey(String lastfmapikey) {
        this.lastfmapikey = lastfmapikey;
    }

    public String getVkappid() {
        return vkappid;
    }

    public void setVkappid(String vkappid) {
        this.vkappid = vkappid;
    }

    public String getVksecretkey() {
        return vksecretkey;
    }

    public void setVksecretkey(String vksecretkey) {
        this.vksecretkey = vksecretkey;
    }

    @NotNull
    public static Config getInstance() {
        return ourInstance;
    }

    private Config() {
        try {
            @NotNull Properties configuration = new Properties();
            @NotNull FileInputStream inputStream = new FileInputStream("./config/main.properties");

            configuration.load(inputStream);

            this.vkappid = configuration.getProperty("vk-app-id");
            this.vksecretkey = configuration.getProperty("vk-secret-key");
            this.lastfmapikey = configuration.getProperty("lastfm-api-key");

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


}
