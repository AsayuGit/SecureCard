package client;

public enum CardStatusCodeEnum {
    // SUCCESS CODES
    SUCCESS((short)0x90),
    DATA_AVAILABLE((short)0x61),

    // FAILURE CODES
    CARD_ERROR((short)0x63);

    public final short code;

    private CardStatusCodeEnum(short code) {
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