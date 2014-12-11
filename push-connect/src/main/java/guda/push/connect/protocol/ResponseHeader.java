package guda.push.connect.protocol;

/**
 * Created by well on 2014/12/11.
 */
public class ResponseHeader {

    private boolean success;

    private String errorMsg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
