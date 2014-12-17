package guda.push.connect.protocol.codec.tlv;

/**
 * Created by foodoon on 2014/12/12.
 */
public class Tag {
    private final byte classes[] = {(byte) 0x00, (byte) 0x40, (byte) 0x80, (byte) 0xC0};

    private int tag;

    private byte tagclass;

    private boolean constructed;

    public Tag() {
        tag = 0;
        tagclass = 0;
        constructed = false;
    }


    public Tag(Tag t) {
        this.tag = t.tag;
        this.tagclass = t.tagclass;
        this.constructed = t.constructed;
    }


    public Tag(int tag, byte tagClass, boolean constructed) {
        this.tag = tag;
        this.tagclass = tagClass;
        this.constructed = constructed;
    }


    public Tag(byte[] binary, int[] offset) {
        fromBinary(binary, offset);
    }

    public Tag(byte[] binary) {
        int[] offset = new int[1];
        offset[0] = 0;
        fromBinary(binary, offset);
    }


    public int size() {
        if (tag < 0x1F) return 1;
        else if (tag < 0x80) return 2;
        else if (tag < 0x4000) return 3;
        else if (tag < 0x200000) return 4;
        return 5;
    }


    public void fromBinary(byte[] binary, int[] offset) {
        // Get class of tag (encoded in bits 7,6 of first byte)
        tagclass = (byte) ((binary[offset[0]] & 0xC0) >>> 6);

        // Get constructed flag (encoded in bit 5 of first byte)
        if ((binary[offset[0]] & (byte) 0x20) == (byte) 0x20) {
            constructed = true;           // This is a constructed TLV
        }else {
            constructed = false;          // This is a primitive TLV
        }
        // Get tag number (encoded in bits 4-0 of first byte and optionally
        // several following bytes.
        tag = 0;
        if ((binary[offset[0]] & (byte) 0x1F) == (byte) 0x1F)
            // it's a multi byte tag
            do {
                offset[0]++;
                tag *= 128;
                tag += binary[offset[0]] & 0x7F;
            } while ((binary[offset[0]] & 0x80) == 0x80);
        else
            // it's a one byte tag
            tag = binary[offset[0]] & (byte) 0x1F;
        offset[0]++;
    }


    public byte[] getBytes() {
        int[] offset = new int[1];
        offset[0] = 0;
        byte[] result = new byte[size()];
        toBinary(result, offset);
        return result;
    }


    public void toBinary(byte[] binary, int[] offset) {
        int count = 0;
        binary[offset[0]] |= classes[tagclass];
        if (constructed) {
            binary[offset[0]] |= 0x20;
        }
        if (tag < 31) {
            binary[offset[0]] |= (byte) tag;
        }else {
            binary[offset[0]] |= 0x1F;
            for (count = this.size() - 2; count > 0; count--) {
                offset[0]++;
                binary[offset[0]] = (byte) (0x80 | ((tag >> (count * 7)) & 0x7f));
            }
            offset[0]++;
            binary[offset[0]] = (byte) (tag & 0x7f);
        }
        offset[0]++;
    }


    public void set(int tag, byte tagclass, boolean constructed) {
        this.tag = tag;
        this.tagclass = tagclass;
        this.constructed = constructed;
    }


    public void setConstructed(boolean constructed) {
        this.constructed = constructed;
    }


    public int code() {
        return tag;
    }


    public boolean isConstructed() {
        return constructed;
    }


    public int hashCode() {
        return tag + tagclass;
    }


    public boolean equals(Object o) {
        return ((this.tag == ((Tag) o).tag) &&
                (this.tagclass == ((Tag) o).tagclass));
    }


    public String toString() {
        return "(" + tag + "," + tagclass + "," + constructed + ")";
    }

}
