package com.musicbox.vkontakte.structure.profiles;

import com.musicbox.vkontakte.OAuthToken;
import com.musicbox.vkontakte.VkontakteClient;

public class Profile {
    private String bdate;
    private String city;
    private String country;
    private String first_name;
    private String last_name;
    private String photo_big;
    private int sex;
    private int uid;

    public OAuthToken getToken() {
        return token;
    }

    public void setToken(OAuthToken token) {
        this.token = token;
    }

    private OAuthToken token;

    public String getBdate() {
        return bdate;
    }

    public void setBdate(String bdate) {
        this.bdate = bdate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhoto_big() {
        return photo_big;
    }

    public void setPhoto_big(String photo_big) {
        this.photo_big = photo_big;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

}
