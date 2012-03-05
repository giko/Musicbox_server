package com.musicbox.server;

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
    private static Config ourInstance = new Config();

    private String vkappid;
    private String vksecretkey;

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

    public static Config getInstance() {
        return ourInstance;
    }

    private Config() {
        try {
            Properties configuration = new Properties();
            FileInputStream inputStream = new FileInputStream("./config/main.properties");

            configuration.load(inputStream);

            this.vkappid = configuration.getProperty("vk-app-id");
            this.vksecretkey = configuration.getProperty("vk-secret-key");

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


}
