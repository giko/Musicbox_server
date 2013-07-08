package com.musicbox.server.packets.outgoing;

import com.musicbox.model.UserEntity;

/**
 * Created with IntelliJ IDEA.
 * User: giko
 * Date: 6/14/13
 * Time: 3:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserPacket extends AbstractOutgoing {
    UserEntity user;

    public UserPacket(UserEntity user) {
        this.user = user;
    }
}
