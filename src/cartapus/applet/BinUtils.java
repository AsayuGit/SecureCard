package applet;

public abstract class BinUtils {
    public static void shortToBytes(short value, byte[] array, short offset) {
        array[offset] = (byte)((value >> 8) & 0xFF);
        array[(short)(offset + 1)] = (byte)(value & 0xFF);
    }

    public static short bytesToShort(byte[] array, short offset) {
        return (short)(((short)(array[offset] & 0xFF) << 8) | (short)(array[(short)(offset + 1)] & 0xFF));
    }
}