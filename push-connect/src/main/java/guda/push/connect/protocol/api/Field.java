package guda.push.connect.protocol.api;

import guda.push.connect.protocol.codec.tlv.Tag;

/**
 * Created by foodoon on 2014/12/12.
 */
public interface Field {

    public final static Tag SEQ = new Tag(0, (byte) 2, false);
    public final static Tag CMD = new Tag(1, (byte) 2, false);
    public final static Tag API_VERSION = new Tag(2, (byte) 2, false);
    public final static Tag USER_AGENT = new Tag(3, (byte) 2, false);

    public final static Tag FROM_HOST = new Tag(5, (byte) 2, false);
    public final static Tag FROM_PORT = new Tag(6, (byte) 2, false);
    public final static Tag FROM_USER = new Tag(7, (byte) 2, false);
    public final static Tag TO_HOST = new Tag(8, (byte) 2, false);
    public final static Tag TO_PORT = new Tag(9, (byte) 2, false);
    public final static Tag TO_USER = new Tag(10, (byte) 2, false);
    public final static Tag CLIENT_HOST = new Tag(11, (byte) 2, false);
    public final static Tag SERVER_HOST = new Tag(12, (byte) 2, false);
    public final static Tag CHAT_CONTENT = new Tag(100, (byte) 2, false);
    public final static Tag BODY = new Tag(99, (byte) 2, false);
}
