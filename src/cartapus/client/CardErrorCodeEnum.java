package client;

public enum CardErrorCodeEnum {
    CARD_EMPTY_ERROR((short)0x01),
    CARD_FULL_ERROR((short)0x02),
    CARD_WRITE_ERROR((short)0x03),
    CARD_FINALIZE_ERROR((short)0x04),
    CARD_READ_ERROR((short)0x05),
    CARD_NO_MORE_FILES_ERROR((short)0x06),
    CARD_CRYPT_ERROR((short)0x07);

    public final short code;

    private CardErrorCodeEnum(short code) {
        this.code = code;
    }

    public static CardStatusCodeEnum get(byte code) throws NoSuchFieldException {
        short searchCode = (short)(code & 0xFF);

        for (CardStatusCodeEnum statusCode : CardStatusCodeEnum.values()) {
            if (statusCode.code == searchCode) {
                return statusCode;
            }
        }

        throw new NoSuchFieldException("Unknown Status Code");
    }
}