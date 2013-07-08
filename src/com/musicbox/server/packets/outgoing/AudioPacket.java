package com.musicbox.server.packets.outgoing;

import com.musicbox.model.vkontakte.structure.audio.Audio;

/**
 * Created with IntelliJ IDEA.
 * User: giko
 * Date: 6/14/13
 * Time: 2:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class AudioPacket extends AbstractOutgoing {
    Audio audio;

    public AudioPacket(Audio audio) {
        this.audio = audio;
    }
}
