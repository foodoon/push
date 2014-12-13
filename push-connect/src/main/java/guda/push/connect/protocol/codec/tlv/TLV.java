package guda.push.connect.protocol.codec.tlv;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by foodoon on 2014/12/12.
 */
public class TLV {

    private Tag tag;
    private int length;
    private byte[] value;
    private TLV parent;
    private TLV sibling;
    private TLV child;
    private TLV lastChild;


    public TLV() {
        tag = new Tag(0, (byte) 0, false);
        length = 0;
        value = null;
        parent = null;
        sibling = null;
        child = null;
        lastChild = null;
    }

    public TLV(byte[] binary) {
        int[] offset = {0};
        tag = new Tag(0, (byte) 0, false);
        length = 0;
        value = null;
        parent = null;
        sibling = null;
        child = null;
        lastChild = null;
        fromBinary(binary, offset, this, null);
    }

    public TLV(Tag tag, byte[] value) {
        int[] offset = {0};
        TLV newTLV = new TLV();

        this.tag = new Tag(tag);
        if (this.tag.isConstructed()) {
            while (offset[0] < value.length)
                fromBinary(value, offset, newTLV, this);
        } else {
            if (value != null)
                this.length = value.length;
            else
                this.length = 0;
            this.value = value;
            this.child = null;
            this.lastChild = null;
        }
        this.parent = null;
        this.sibling = null;
    }

    public TLV(Tag tag, int number) {
        int i = 0;
        this.tag = new Tag(tag);
        if (number < 0x100) {
            value = new byte[1];
        } else if (number < 0x10000) {
            value = new byte[2];
        } else if (number < 0x1000000) {
            value = new byte[3];
        } else {
            value = new byte[4];
        }

        for (i = value.length - 1; i >= 0; i--) {
            value[i] = (byte) (number % 0x100);
            number /= 0x100;
        }

        this.length = value.length;
        this.child = null;
        this.lastChild = null;
        this.parent = null;
        this.sibling = null;
    }

    public TLV(Tag tag, TLV tlv) {
        this.tag = new Tag(tag);
        this.tag.setConstructed(true);
        this.value = null;
        this.parent = null;
        this.sibling = null;
        this.child = tlv;
        this.lastChild = tlv;

        if (tlv != null)
            this.length = tlv.tag.size() + tlv.lenBytes() + tlv.length;
        else
            this.length = 0;
    }

    public TLV add(TLV tlv) {
        TLV iterTLV;
        int originalReprLength = 0;
        int deltaReprLength = 0;

        if (tag.isConstructed() == true) {
            tlv.parent = this;
            tlv.sibling = null;
            if (lastChild != null) {
                lastChild.sibling = tlv;
            }
            lastChild = tlv;

            iterTLV = this;
            while (iterTLV != null) {
                originalReprLength = iterTLV.lenBytes();
                iterTLV.length += tlv.length + tlv.tag.size() + tlv.lenBytes() + deltaReprLength;
                deltaReprLength += iterTLV.lenBytes() - originalReprLength;
                iterTLV = iterTLV.parent;
            }
            return this;
        } else {
            return null;
        }
    }

    public TLV findTag(Tag tag, TLV cursor) {
        TLV iterTLV;

        if (cursor == null)
            iterTLV = child;
        else
            iterTLV = cursor.sibling;

        if (tag == null)
            return iterTLV;

        while (iterTLV != null) {
            if (iterTLV.tag.equals(tag))
                return iterTLV;
            iterTLV = iterTLV.sibling;
        }
        return null;
    }

    public static void fromBinary(byte[] binary, int[] offset, TLV tlv, TLV parent) {
        int i = 0;
        int oldOffset = offset[0];
        TLV iterTLV = null;

        tlv.tag.fromBinary(binary, offset);

        tlv.length = 0;


        if ((binary[offset[0]] & (byte) 0x80) == (byte) 0x00) {
            tlv.length += (int) binary[offset[0]];
        } else {
            int numBytes = (binary[offset[0]] & (byte) 0x7F);
            int j = 0;
            while (numBytes > 0) {
                offset[0]++;
                j = binary[offset[0]];
                tlv.length += (j < 0 ? j += 256 : j);

                if (numBytes > 1) tlv.length *= 256;
                numBytes--;
            }
        }
        offset[0]++;

        if (tlv.tag.isConstructed()) {
            tlv.value = null;
            tlv.child = new TLV();
            fromBinary(binary, offset, tlv.child, tlv);

            iterTLV = tlv.child;
            while (offset[0] <= oldOffset + tlv.length) {
                iterTLV.sibling = new TLV();
                fromBinary(binary, offset, iterTLV.sibling, tlv);
                iterTLV = iterTLV.sibling;
            }
            tlv.lastChild = iterTLV;
        } else {
            tlv.child = null;
            tlv.sibling = null;            // The new TLV has no sibling.
            tlv.value = new byte[tlv.length];
            System.arraycopy(binary, offset[0], tlv.value, 0, tlv.length);
            offset[0] += tlv.length;
        }
        tlv.parent = parent;
    }

    private int lenBytes() {
        if (length < 0x80) return 1;
        else if (length < 0x100) return 2;
        else if (length < 0x10000) return 3;
        else if (length < 0x1000000) return 4;
        else return 5;
    }

    public static int lenBytes(int length) {
        if (length < 0x80) return 1;
        else if (length < 0x100) return 2;
        else if (length < 0x10000) return 3;
        else if (length < 0x1000000) return 4;
        else return 5;
    }

    public int length() {
        return length;
    }

    public static byte[] lengthToBinary(int length) {
        byte[] binary = new byte[lenBytes(length)];
        if (length < 0x80) {
            binary[0] = (byte) length;
        } else if (length < 0x100) {
            binary[0] = (byte) 0x81;
            binary[1] = (byte) length;
        } else if (length < 0x10000) {
            binary[0] = (byte) 0x82;
            binary[1] = (byte) (length / 0x100);
            binary[2] = (byte) (length % 0x100);
        } else if (length < 0x1000000) {
            binary[0] = (byte) 0x83;
            binary[1] = (byte) (length / 0x10000);
            binary[2] = (byte) (length / 0x100);
            binary[3] = (byte) (length % 0x100);
        }
        return binary;
    }

    public void setValue(byte[] newValue) {
        int originalReprLength = 0;
        int deltaReprLength = 0;

        originalReprLength = this.lenBytes();

        int oldLength = length;
        value = newValue;
        if (newValue != null)
            length = value.length;
        else
            length = 0;

        deltaReprLength = this.lenBytes() - originalReprLength;

        TLV iterTLV = this.parent;
        while (iterTLV != null) {
            originalReprLength = iterTLV.lenBytes();
            iterTLV.length += (length - oldLength) + deltaReprLength;
            ;
            deltaReprLength += iterTLV.lenBytes() - originalReprLength;
            iterTLV = iterTLV.parent;
        }
    }

    public Tag tag() {
        return tag;
    }

    public byte[] toBinary() {
        int[] offset = {0};
        int totalLength = tag.size() + lenBytes() + length;
        byte binary[] = new byte[totalLength];
        this.toBinaryHelper(binary, offset, totalLength);
        return binary;
    }

    public byte[] toBinaryContent() {
        int[] offset = {0};
        int totalLength = length;
        byte binary[] = new byte[totalLength];
        this.toBinaryHelperContent(binary, offset, totalLength);
        return binary;
    }

    private void toBinaryHelper(byte[] binary, int[] offset, int max) {
        int i = 0;

        tag.toBinary(binary, offset);
        toBinaryLength(binary, offset);
        if (child != null)
            child.toBinaryHelper(binary, offset, max);
        else if (value != null) {
            System.arraycopy(value, 0, binary, offset[0], value.length);
            offset[0] += value.length;
        }


        if (sibling != null && offset[0] < max) {
            sibling.toBinaryHelper(binary, offset, max);
        }
    }

    private void toBinaryHelperContent(byte[] binary, int[] offset, int max) {
        int i = 0;

        if (child != null)
            child.toBinaryHelper(binary, offset, max);
        else {
            if (value != null) {
                System.arraycopy(value, 0, binary, offset[0], value.length);
                offset[0] += value.length;
            }
        }
    }

    private void toBinaryLength(byte[] binary, int[] offset) {
        if (length < 0x80) {
            binary[offset[0]] = (byte) length;
        } else if (length < 0x100) {
            binary[offset[0]] = (byte) 0x81;
            offset[0]++;
            binary[offset[0]] = (byte) length;
        } else if (length < 0x10000) {
            binary[offset[0]] = (byte) 0x82;
            offset[0]++;
            binary[offset[0]] = (byte) (length / 0x100);
            offset[0]++;
            binary[offset[0]] = (byte) (length % 0x100);
        } else if (length < 0x1000000) {
            binary[offset[0]] = (byte) 0x83;
            offset[0]++;
            binary[offset[0]] = (byte) (length / 0x10000);
            offset[0]++;
            binary[offset[0]] = (byte) (length / 0x100);
            offset[0]++;
            binary[offset[0]] = (byte) (length % 0x100);
        }
        offset[0]++;
    }

    public String toString() {
        return toString(null, 0);
    }

    public String toString(ConcurrentHashMap ht, int level) {
        StringBuilder s = new StringBuilder();
        int i = 0;
        for (i = 0; i < level; i++) {
            s.append(" ");
        }
        if (ht == null) {
            s.append("[").append(tag).append(" ").append(length).append("] ");
        }else {
            s.append( ht.get(tag)).append( " ");
        }
        if (tag.isConstructed()) {
            s.append( "\n");
            for (i = 0; i < level; i++)
                s.append( " ");
        }
        s.append( "( ");

        if (tag.isConstructed()) {
            s.append( "\n");
            s.append( child.toString(ht, level + 2));
            for (i = 0; i < level; i++) {
                s.append(" ");
            }
            s.append(")\n");
        } else {
            boolean fPrintable = true;
            if (value != null) {
                for (i = 0; i < value.length; i++)
                    if (value[i] < 32) fPrintable = false;
                if (fPrintable)
                    s.append("\"") .append(new String(value)).append( "\"");
                else {
                    s.append( "'");
                    for (i = 0; i < value.length; i++) {
                        s.append(HexString.hexify(value[i]));
                    }
                    s.append("'");
                }
            }
            s.append(" )\n");
        }

        if (sibling != null) {
            s.append(sibling.toString(ht, level));
        }
        return s.toString();
    }

    public byte[] valueAsByteArray() {
        return value;
    }

    public int valueAsInt() {
        int i = 0;
        int j = 0;
        int number = 0;

        for (i = 0; i < value.length; i++) {
            j = value[i];
            number = number * 256 + (j < 0 ? j += 256 : j);
        }
        return number;
    }

    public long valueAsLong() {
        int i = 0;
        int j = 0;
        long number = 0;

        for (i = 0; i < value.length; i++) {
            j = value[i];
            number = number * 256 + (j < 0 ? j += 256 : j);
        }
        return number;
    }
}
