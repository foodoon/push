package guda.push.server.biz;

import guda.push.connect.msg.MsgFactory;
import guda.push.connect.protocol.api.Command;
import guda.push.connect.protocol.api.Field;
import guda.push.connect.protocol.api.Struct;
import guda.push.connect.protocol.codec.CodecUtil;
import guda.push.connect.protocol.codec.tlv.TLV;
import guda.push.connect.protocol.codec.tlv.TypeConvert;
import guda.push.connect.udp.host.HostInfo;
import guda.push.connect.udp.host.OnlineInfo;

/**
 * Created by foodoon on 2014/12/12.
 */
public class ChatBiz implements Biz {

    public static final int command = Command.CHAT;

    @Override
    public void service(TLV request) {
        if(request == null){
            return ;
        }
        TLV tlv = CodecUtil.newTlv(Struct.CHAT);
        String content = CodecUtil.findTagString(request, Field.CHAT_CONTENT);
        long fromUser = CodecUtil.findTagLong(request, Field.FROM_USER);
        long toUser = CodecUtil.findTagLong(request, Field.TO_USER);
        content += "from:" + fromUser + ":" + content;
        tlv.add(new TLV(Field.FROM_USER, TypeConvert.long2byte(fromUser)));
        tlv.add(new TLV(Field.TO_USER, TypeConvert.long2byte(toUser)));
        tlv.add(new TLV(Field.CHAT_CONTENT, TypeConvert.string2byte(content)));
        HostInfo onlineInfo = OnlineInfo.findOnlineInfo(toUser);
        if(onlineInfo != null){
            tlv.add(new TLV(Field.TO_HOST, TypeConvert.string2byte(onlineInfo.getHost())));
            tlv.add(new TLV(Field.TO_PORT, TypeConvert.int2byte(onlineInfo.getPort())));
        }
        MsgFactory.addUdpRoute(tlv);

    }
}
