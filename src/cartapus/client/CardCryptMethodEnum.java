package client;

// Enum listing all methods available on the cars's ModuleCrypt
public enum CardCryptMethodEnum implements ICardMethodEnum {
    SetKey((byte)0x00),
    Init((byte)0x01),
    Update((byte)0x02),
    Finalize((byte)0x03);

    public final byte method;

    private CardCryptMethodEnum(byte method) {
        this.method = method;
    }

    @Override
    public byte getMethod() {
        return method;
    }
}