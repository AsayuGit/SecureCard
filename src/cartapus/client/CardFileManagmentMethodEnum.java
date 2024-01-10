package client;

public enum CardFileManagmentMethodEnum implements ICardMethodEnum {
    RewindFiles((byte)0x00),
    NextFile((byte)0x01);

    public final byte method;

    private CardFileManagmentMethodEnum(byte method) {
        this.method = method;
    }

    @Override
    public byte getMethod() {
        return method;
    }
}