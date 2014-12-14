package guda.push.connect.protocol.api;

import guda.push.connect.protocol.codec.tlv.Tag;

/**
 * Created by foodoon on 2014/12/12.
 */
public class Struct {

    public final static Tag ACK = new Tag(Command.ACK, (byte) 2, false);
    public final static Tag HEARBEAT = new Tag(Command.HEARBEAT, (byte) 2, false);
    public final static Tag CHAT = new Tag(Command.CHAT, (byte) 2, false);

    public final static Tag NOTICE = new Tag(Command.NOTICE, (byte) 2, false);
}
