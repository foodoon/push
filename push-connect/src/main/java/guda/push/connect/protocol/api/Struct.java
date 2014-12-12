package guda.push.connect.protocol.api;

import guda.push.connect.protocol.codec.tlv.Tag;

/**
 * Created by foodoon on 2014/12/12.
 */
public class Struct {

    public final static Tag ACK = new Tag(1, (byte) 2, false);

    public final static Tag CHAT = new Tag(100, (byte) 2, false);

    public final static Tag NOTICE = new Tag(101, (byte) 2, false);
}
