package com.musicbox.model.vkontakte;

import com.musicbox.ExcludeFromSerialisation;
import com.musicbox.model.vkontakte.structure.profiles.Profile;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ouathtoken", schema = "public", catalog = "musicbox")
public class OAuthToken implements Serializable {
    private String access_token;
    private int expires_in;
    @Id
    private int user_id;
    @OneToOne(mappedBy = "token", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ExcludeFromSerialisation
    private Profile profile;
    @Transient
    private String error;
    @Transient
    private String error_description;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError_description() {
        return error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
