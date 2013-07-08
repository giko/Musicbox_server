package com.musicbox.server.packets.outgoing;

import com.musicbox.server.Config;

/**
 * Created with IntelliJ IDEA.
 * User: giko
 * Date: 6/14/13
 * Time: 2:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class RedirectToVKPacket extends AbstractOutgoing {
    String vkappid = Config.getInstance().getVkappid();
}
