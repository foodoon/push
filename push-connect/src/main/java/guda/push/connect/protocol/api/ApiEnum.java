package guda.push.connect.protocol.api;

/**
 * Created by well on 2014/12/11.
 */
public enum ApiEnum {

    CHAT(100,Chat.class);

    private int id;
    private Class clz;
    private ApiEnum(int id,Class clz){
        this.id =  id;
        this.clz = clz;
    }

}
