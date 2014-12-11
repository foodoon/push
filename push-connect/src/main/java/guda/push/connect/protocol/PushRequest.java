package guda.push.connect.protocol;

/**
 * Created by well on 2014/12/11.
 */
public class PushRequest {

    private String id;

    private RequestHeader pushHeader;

    private Object body;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RequestHeader getPushHeader() {
        return pushHeader;
    }

    public void setPushHeader(RequestHeader pushHeader) {
        this.pushHeader = pushHeader;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
