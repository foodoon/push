package guda.push.connect.protocol.api;

/**
 * Created by well on 2014/12/11.
 */
public class Notice {

    private long receiver;

    private int msgType;

    private String content;

    public long getReceiver() {
        return receiver;
    }

    public void setReceiver(long receiver) {
        this.receiver = receiver;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
