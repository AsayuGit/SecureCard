package client;

public enum CardModuleEnum {
    FILE_MANAGEMENT((byte)0x01),
    FILE_WRITE((byte)0x02),
    FILE_READ((byte)0x03),
    CRYPT((byte)0x04);

    public final byte module;

    private CardModuleEnum(byte module) {
        this.module = module;
    }
}