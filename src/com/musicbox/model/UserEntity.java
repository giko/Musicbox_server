package com.musicbox.model;

import com.musicbox.model.vkontakte.structure.profiles.Profile;

import javax.persistence.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: giko
 * Date: 3/8/13
 * Time: 12:45 AM
 * To change this template use File | Settings | File Templates.
 */
@Table(name = "user", schema = "public", catalog = "musicbox", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "vkid"})})
@Entity
public class UserEntity {
    @javax.persistence.Column(name = "id")
    @Id
    @GeneratedValue
    private int id;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "vkid", nullable = false)
    private Profile profile;
    private String name;
    @OneToMany(mappedBy = "user")
    private List<LoginTokenEntity> loginToken;

    public void addLoginToken(LoginTokenEntity tokenEntity){
        loginToken.add(tokenEntity);
        tokenEntity.setUser(this);
    }

    public List<LoginTokenEntity> getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(List<LoginTokenEntity> loginToken) {
        this.loginToken = loginToken;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @javax.persistence.Column(name = "name")
    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
