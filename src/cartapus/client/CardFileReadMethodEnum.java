package client;

public enum CardFileReadMethodEnum implements ICardMethodEnum {
    TransferInit((byte)0x00),
    ReadData((byte)0x01);

    public final byte method;

    private CardFileReadMethodEnum(byte method) {
        this.method = method;
    }

    @Override
    public byte getMethod() {
        return method;
    }
}