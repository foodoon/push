package guda.push.connect.protocol.codec.tlv;

/**
 * Created by foodoon on 2014/12/11.
 */
public class TlvUtil {

    public static byte[] tag2byte(int tagValue, int frameType, int dataType) {
        int size = 1;
        int rawTag = frameType | dataType | tagValue;
        if (tagValue < 0x1F) {
            // 1 byte tag
            rawTag = frameType | dataType | tagValue;
        } else {
            // mutli byte tag
            rawTag = frameType | dataType | 0x1F;
            if (tagValue < 0x80) {
                rawTag <<= 8;
                rawTag |= tagValue & 0x7F;
            } else if (tagValue < 0x3FFF) {
                rawTag <<= 16;
                rawTag |= (((tagValue & 0x3FFF) >> 7 & 0x7F) | 0x80) << 8;
                rawTag |= ((tagValue & 0x3FFF) & 0x7F);
            } else if (tagValue < 0x3FFFF) {
                rawTag <<= 24;
                rawTag |= (((tagValue & 0x3FFFF) >> 14 & 0x7F) | 0x80) << 16;
                rawTag |= (((tagValue & 0x3FFFF) >> 7 & 0x7F) | 0x80) << 8;
                rawTag |= ((tagValue & 0x3FFFF) & 0x7F);
            }
        }
        return TypeConvert.int2byte(rawTag);
    }

    public static byte[] length2byte(int length) {
        if (length < 0) {
            throw new IllegalArgumentException();
        } else
            // 短形式
            if (length < 128) {
                byte[] actual = new byte[1];
                actual[0] = (byte) length;
                return actual;
            } else
                // 长形式
                if (length < 256) {
                    byte[] actual = new byte[2];
                    actual[0] = (byte) 0x81;
                    actual[1] = (byte) length;
                    return actual;
                } else if (length < 65536) {
                    byte[] actual = new byte[3];
                    actual[0] = (byte) 0x82;
                    actual[1] = (byte) (length >> 8);
                    actual[2] = (byte) length;
                    return actual;
                } else if (length < 16777126) {
                    byte[] actual = new byte[4];
                    actual[0] = (byte) 0x83;
                    actual[1] = (byte) (length >> 16);
                    actual[2] = (byte) (length >> 8);
                    actual[3] = (byte) length;
                    return actual;
                } else {
                    byte[] actual = new byte[5];
                    actual[0] = (byte) 0x84;
                    actual[1] = (byte) (length >> 24);
                    actual[2] = (byte) (length >> 16);
                    actual[3] = (byte) (length >> 8);
                    actual[4] = (byte) length;
                    return actual;
                }
    }


}
