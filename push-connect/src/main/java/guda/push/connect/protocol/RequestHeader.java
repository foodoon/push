package guda.push.connect.protocol;

/**
 * Created by well on 2014/12/11.
 */
public class RequestHeader {

    private int apiVersion;

    private  int apiName;

    public int getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(int apiVersion) {
        this.apiVersion = apiVersion;
    }

    public int getApiName() {
        return apiName;
    }

    public void setApiName(int apiName) {
        this.apiName = apiName;
    }
}
