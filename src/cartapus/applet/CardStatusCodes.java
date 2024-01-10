package applet;

import javacard.framework.ISOException;

public abstract class CardStatusCodes {
    // SUCCESS CODES

    // FAILURE CODES
    public static final short CARD_ERROR = (short)0x6300;
    public static final short CARD_EMPTY = (short)0x6301;
    public static final short CARD_FULL = (short)0x6302;
    public static final short WRITE_ERROR = (short)0x6303;
    public static final short FINALIZE_ERROR = (short)0x6304;
    public static final short READ_ERROR = (short)0x6305;
    public static final short NO_MORE_FILES = (short)0x6306;
    public static final short CARD_CRYPT_ERROR = (short)0x6307;
    public static final short EXPECTED_PAYLOAD_EMPTY = (short)0x6308;

    public static void SendStatus(short status) {
        ISOException.throwIt(status);
    }
}