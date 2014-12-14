package guda.push.connect.queue;

import guda.push.connect.protocol.codec.tlv.TLV;

/**
 * Created by foodoon on 2014/12/14.
 */
public class AckTLV {

    private TLV tlv;

    private long lastModify;

    public AckTLV(){

    }
    public AckTLV(TLV tlv){
        this.tlv = tlv;
        lastModify = System.currentTimeMillis();
    }

    public void refresh(){
        lastModify = System.currentTimeMillis();
    }

    public boolean needRetry(){
        return System.currentTimeMillis()-lastModify>5000;
    }

    public TLV getTlv() {
        return tlv;
    }

    public void setTlv(TLV tlv) {
        this.tlv = tlv;
    }

    public long getLastModify() {
        return lastModify;
    }

    public void setLastModify(long lastModify) {
        this.lastModify = lastModify;
    }
}
