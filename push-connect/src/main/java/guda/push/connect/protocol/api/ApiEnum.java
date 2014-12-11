package guda.push.connect.protocol.api;

/**
 * Created by well on 2014/12/11.
 */
public enum ApiEnum {

    NOTICE((short)100,Notice.class),
    CHAT((short)200,Chat.class),
    ;

    private short id;
    private Class clz;
    private ApiEnum(short id,Class clz){
        this.id =  id;
        this.clz = clz;
    }

    public static Class getById(short id){
        ApiEnum[] values = ApiEnum.values();
        for(ApiEnum apiEnum:values){
            if(apiEnum.id == id){
                return apiEnum.clz;
            }
        }
        return null;
    }

}
