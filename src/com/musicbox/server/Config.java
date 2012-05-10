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
    private boolean lastfmshowdebugginginfo;
    private boolean webbitdebug;

    public boolean isWebbitdebug() {
        return webbitdebug;
    }

    public void setWebbitdebug(boolean webbitdebug) {
        this.webbitdebug = webbitdebug;
    }

    public boolean isLastfmshowdebugginginfo() {
        return lastfmshowdebugginginfo;
    }

    public void setLastfmshowdebugginginfo(boolean lastfmshowdebugginginfo) {
        this.lastfmshowdebugginginfo = lastfmshowdebugginginfo;
    }

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
            this.lastfmshowdebugginginfo = Boolean.parseBoolean(configuration.getProperty("lastfm-show-debugging-info", "false"));
            this.webbitdebug = Boolean.parseBoolean(configuration.getProperty("webbit-debug", "true"));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


}
