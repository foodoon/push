package guda.push.server.biz;

import guda.push.connect.protocol.codec.tlv.TLV;

/**
 * Created by foodoon on 2014/12/12.
 */
public interface Biz {

    public void service(TLV request);

}
