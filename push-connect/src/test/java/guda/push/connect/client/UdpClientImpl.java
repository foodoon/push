package guda.push.connect.client;

import guda.push.connect.protocol.codec.tlv.TLV;

/**
 * Created by foodoon on 2015/1/21.
 */
public class UdpClientImpl extends UdpClient{
    public UdpClientImpl(String host, int port, long userId) throws Exception {
        super(host, port, userId);
    }

    @Override
    public void onReceiverMsg(TLV tlv) {
        System.out.println("recv:"+tlv);
    }
}
