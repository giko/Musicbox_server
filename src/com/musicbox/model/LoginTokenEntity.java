package com.musicbox.model;

import com.musicbox.model.vkontakte.OAuthToken;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: giko
 * Date: 3/9/13
 * Time: 1:34 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "logintoken", schema = "public", catalog = "musicbox")
public class LoginTokenEntity {
    @Id
    private String token;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "oauthtokenid")
    private OAuthToken oAuthToken;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "userid")
    private UserEntity user;

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public OAuthToken getoAuthToken() {
        return oAuthToken;
    }

    public void setoAuthToken(OAuthToken oAuthToken) {
        this.oAuthToken = oAuthToken;
    }
}
