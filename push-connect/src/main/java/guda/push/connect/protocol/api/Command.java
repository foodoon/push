package guda.push.connect.protocol.api;

/**
 * Created by foodoon on 2014/12/12.
 */
public class Command {

    //内部指令
    public static final int S_LOGIN_SUCCESS = 0;
    public static final int S_LOGIN_OUT_SUCCESS = 1;
    //业务指令
    public static final int ACK = 100;
    public static final int HEARBEAT = 101;
    public static final int CHAT = 200;
    public static final int NOTICE = 201;

}
