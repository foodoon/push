package guda.push.connect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;

/**
 * Created by foodoon on 2014/12/12.
 */
public class TestJson {

    public static void main(String[] args){
        ChatTest chatTest = new ChatTest();
        chatTest.setApiVersion((short)1);
        chatTest.setBody("body");
        chatTest.setClientIp("192.168.7.25");
        chatTest.setCmd((short)1);
        chatTest.setUserAgent("fire fox client");
        chatTest.setSeq(3L);
        byte[] bytes = null;
        long start =System.currentTimeMillis();
        for(int i=0;i<10000;++i) {
            bytes = JSON.toJSONBytes(chatTest);
        }
        System.out.println(System.currentTimeMillis()-start);
        start =System.currentTimeMillis();
        for(int i=0;i<10000;++i) {
            JSONObject jsonObject = JSON.parseObject(new String(bytes), Feature.IgnoreNotMatch);
            jsonObject.get("clientIp");
        }
        System.out.println(System.currentTimeMillis()-start);
        System.out.println(JSON.toJSONBytes(chatTest).length);
    }
}
