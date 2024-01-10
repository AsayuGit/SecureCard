package client;

// The BinUtil class provides byte manipulation methods
public abstract class BinUtils {
    public static void shortToBytes(short value, byte[] array, short offset) {
        array[offset] = (byte)((value >> 8) & 0xFF);
        array[(short)(offset + 1)] = (byte)(value & 0xFF);
    }

    public static short bytesToShort(byte[] array, short offset) {
        return (short)(((short)(array[offset] & 0xFF) << 8) | (short)(array[(short)(offset + 1)] & 0xFF));
    }

    private static int B = 1;
    private static int KiB = B << 10;
    private static int MiB = KiB << 10;

    public static String toHumanReadable(short size) {
        if (size > MiB) {
            return (size / MiB) + " MiB";
        } else if (size > KiB) {
            return (size / KiB) + " KiB";
        } else {
            return size + " B";
        }
    }
}