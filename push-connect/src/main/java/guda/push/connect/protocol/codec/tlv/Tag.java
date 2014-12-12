package guda.push.connect.protocol.codec.tlv;

/**
 * Created by foodoon on 2014/12/12.
 */
public class Tag {
    /** tag number */
    private int     tag;
    /** tag class, 0 - 4 */
    private byte    tagclass;
    /** constructed flag */
    private boolean constructed;

    /** Create a null tag.<p>
     */
    public Tag() {
        tag         = 0;
        tagclass    = 0;
        constructed = false;
    }

    /** Clone a tag.<p>
     *
     * @param t
     *        The <tt>Tag</tt> object to be cloned.
     */
    public Tag(Tag t) {
        this.tag         = t.tag;
        this.tagclass    = t.tagclass;
        this.constructed = t.constructed;
    }

    /** Creates a tag from a given tag value, class and constructed flag.<p>
     *
     * @param tag
     *        An integer representing the value of the tag.
     * @param tagClass
     *        A byte value representing the class of the tag.
     * @param constructed
     *        A boolean value <tt>true</tt> signals that the tag is
     *        constructed, <tt>false</tt> signals that the tag is
     *        primitive.
     */
    public Tag(int tag, byte tagClass, boolean constructed) {
        this.tag         = tag;
        this.tagclass    = tagClass;
        this.constructed = constructed;
    }

    /** Create a tag from binary representation.<p>
     *
     * @param binary
     *        The byte array from which the tag shall be generated.
     * @param offset
     *        An integer value giving the offset into the the byte array
     *        from where to start.
     */
    public Tag(byte[] binary, int[] offset) {
        fromBinary(binary, offset);
    }

    public Tag(byte[] binary) {
        int[] offset = new int[1];
        offset[0] = 0;
        fromBinary(binary, offset);
    }

    /** Return the number of bytes which are required to BER-code
     * the tag value.<p>
     *
     * @return An integer giving the number of bytes.
     */
    public int size() {
        if      (tag < 0x1F)       return 1;
        else if (tag < 0x80)       return 2;
        else if (tag < 0x4000)     return 3;
        else if (tag < 0x200000)   return 4;
        return 5;
    }

    /** Initialize the <tt>Tag</tt> object from a BER-coded
     * binary representation.<p>
     *
     * @param binary
     *        A byte array containing the BER-coded tag.
     * @param offset
     *        An integer giving an offset into the byte array
     *        from where to start.
     */
    public void fromBinary(byte[] binary, int[] offset) {
        // Get class of tag (encoded in bits 7,6 of first byte)
        tagclass = (byte) ((binary[offset[0]] & 0xC0) >>> 6);

        // Get constructed flag (encoded in bit 5 of first byte)
        if ((binary[offset[0]] & (byte) 0x20) == (byte) 0x20)
            constructed = true;           // This is a constructed TLV
        else
            constructed = false;          // This is a primitive TLV

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

    /**
     * Gets a byte array representing the tag.
     *
     * @return the tag as a byte array
     */
    public byte[] getBytes()
    {
        int[] offset = new int[1];
        offset[0] = 0;
        byte[] result = new byte[size()];
        toBinary(result, offset);
        return result;
    }

    /** Convert the tag to binary representation.<p>
     *
     * @param binary
     *        A byte array to which the BER-coded binary representation
     *        of the tag shall be written.
     * @param offset
     *        An integer value giving an offset into the byte array from
     *        where to start.
     */
    public void toBinary(byte[] binary, int[] offset) {
        byte classes[] = {(byte)0x00, (byte)0x40, (byte)0x80, (byte)0xC0};
        int count = 0;

        binary[offset[0]] |= classes[tagclass];  // encode class
        if (constructed)                   // encode constructed bit
            binary[offset[0]] |= 0x20;

        if (tag < 31)                      // encode tag number
            binary[offset[0]] |= (byte) tag;
        else {
            binary[offset[0]] |= 0x1F;
            for (count = this.size()-2; count > 0; count--) {
                offset[0]++;
                binary[offset[0]] = (byte) ( 0x80 | (( tag >> (count * 7)) & 0x7f));
            }
            offset[0]++;
            binary[offset[0]] = (byte) (tag & 0x7f);
        }
        offset[0]++;
    }

    /** Set the tag number, class and constructed flag of this
     * <tt>Tag</tt> to the given values.<p>
     *
     * @param tag
     *        An integer value giving the tag value.
     * @param tagclass
     *        A byte value giving the class.
     * @param constructed
     *        A boolean representing the constructed flag.
     */
    public void set(int tag, byte tagclass, boolean constructed) {
        this.tag         = tag;
        this.tagclass    = tagclass;
        this.constructed = constructed;
    }

    /** Set the constructed flag of this <tt>Tag</tt> to the given value.<p>
     *
     * @param constructed
     *        A boolean representing the constructed flag.
     */
    public void setConstructed(boolean constructed) {
        this.constructed = constructed;
    }

    /** Get the code of the tag.<p>
     *
     * @return An integer value representing the tag's code.
     */
    public int code() {
        return tag;
    }

    /** Check whether this <tt>Tag</tt> is constructed.<p>
     *
     * @return <tt>true</tt> if it is constructed, <tt>false</tt> otherwise.
     */
    public boolean isConstructed() {
        return constructed;
    }

    /** Compute a hash code for this tag.<p>
     *
     * @return An integer value representing the hash code.
     */
    public int hashCode() {
        return tag+tagclass;
    }

    /** Check for equality.<p>
     *
     * @return <tt>true</tt>, if this <tt>Tag</tt> instance equals the given
     *         tag, <tt>false</tt> otherwise.
     */
    public boolean equals(Object o) {
        return ((this.tag          == ((Tag) o).tag) &&
                (this.tagclass     == ((Tag) o).tagclass));
    }

    /** Get a string representation for this tag.<p>
     *
     * @return The string representation.
     */
    public String toString() {
        return "(" + tag + "," + tagclass + "," + constructed + ")";
    }

}
