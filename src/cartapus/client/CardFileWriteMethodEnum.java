package client;

public enum CardFileWriteMethodEnum implements ICardMethodEnum {
    TransferInit((byte)0x00),
    WriteData((byte)0x01),
    Finalize((byte)0x02);

    public final byte method;

    private CardFileWriteMethodEnum(byte method) {
        this.method = method;
    }

    @Override
    public byte getMethod() {
        return method;
    }
}