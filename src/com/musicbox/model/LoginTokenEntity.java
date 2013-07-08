package com.musicbox.model;

import com.musicbox.ExcludeFromSerialisation;
import com.musicbox.model.vkontakte.OAuthToken;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: giko
 * Date: 3/9/13
 * Time: 1:34 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "logintoken", schema = "public", catalog = "musicbox")
public class LoginTokenEntity implements Serializable{
    @Id
    private String token;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "oauthtokenid", nullable = false)
    private OAuthToken oAuthToken;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "userid", nullable = false)
    @ExcludeFromSerialisation
    private UserEntity user;

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        if (user.getLoginToken() == null){
            user.setLoginToken(new ArrayList<LoginTokenEntity>());
        }
        user.getLoginToken().add(this);
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
