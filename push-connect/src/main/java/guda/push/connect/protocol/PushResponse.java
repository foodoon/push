package guda.push.connect.protocol;

/**
 * Created by well on 2014/12/11.
 */
public class PushResponse<T> {

    private ResponseHeader header;

    private T body;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ResponseHeader getHeader() {
        return header;
    }

    public void setHeader(ResponseHeader header) {
        this.header = header;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
