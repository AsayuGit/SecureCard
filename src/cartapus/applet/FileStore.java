package applet;

import javacard.framework.Util;
import javacard.framework.JCSystem;

public class FileStore {
    // Write Data pointers
    private static final byte WRITE_DATA_PTR = (byte)0x00; // Offset relative to writeBaseDataPtr

    // Temporary write context
    private static final byte NAME_SIZE = (byte)0x01;
    private static final byte DATA_SIZE = (byte)0x02;
    private static final byte WRITE_TRANSFER_SIZE = (byte)0x03;

    // Read Data pointers
    private static final byte READ_BASE_DATA_PTR = (byte)0x04;
    private static final byte READ_DATA_PTR = (byte)0x05;

    // Temporary read context
    private static final byte READ_TRANSFER_SIZE = (byte)0x06;

    // Non-Volatile Data
    private final short FileStoreSize;
    private final byte[] FileStoreBuffer;
    private short writeBaseDataPtr = (short)0x00;

    // Volatile memory array (Cleared on deselect)
    private final short[] ram;

    public FileStore(short size) {
        ram = JCSystem.makeTransientShortArray((short)7, JCSystem.CLEAR_ON_DESELECT);

        FileStoreSize = size;
        FileStoreBuffer = new byte[FileStoreSize];
    }

    public boolean initWriteTransfer(short nameSize, short dataSize) {
        ram[WRITE_TRANSFER_SIZE] = (short)(nameSize + dataSize + 4);

        // Check for free space
        if ((nameSize < 0) || (dataSize < 0)) return false;
        if (ram[WRITE_TRANSFER_SIZE] > (short)(FileStoreSize - writeBaseDataPtr)) return false;

        // Initialize the data pointer (With space reserved for the data size)
        ram[WRITE_DATA_PTR] = (short)4;

        // Init temp data vars
        ram[NAME_SIZE] = nameSize;
        ram[DATA_SIZE] = dataSize;

        return true;
    }

    public boolean write(byte[] buffer, short offset, short size) {
        if ((short)(ram[WRITE_DATA_PTR] + size) > ram[WRITE_TRANSFER_SIZE]) {
            return false;
        } else {
            // Write data to the fileStore
            Util.arrayCopy(buffer, offset, FileStoreBuffer, (short)(writeBaseDataPtr + ram[WRITE_DATA_PTR]), size);

            // Then move the data pointer
            ram[WRITE_DATA_PTR] += size;

            return true;
        }
    }

    public boolean closeTranfer() {
        // Check if all data were transfered
        if (ram[WRITE_DATA_PTR] != ram[WRITE_TRANSFER_SIZE]) return false;

        // Write the filename size
        BinUtils.shortToBytes(ram[NAME_SIZE], FileStoreBuffer, writeBaseDataPtr);

        // Write the data size
        BinUtils.shortToBytes(ram[DATA_SIZE], FileStoreBuffer, (short)(writeBaseDataPtr + 2));
        writeBaseDataPtr += ram[WRITE_TRANSFER_SIZE];

        return true;
    }

    public boolean rewindFiles() {
        ram[READ_BASE_DATA_PTR] = (short)0x00;
        return (ram[READ_BASE_DATA_PTR] < writeBaseDataPtr);
    }

    public boolean nextFile() {
        if (ram[READ_BASE_DATA_PTR] > writeBaseDataPtr) {
            return false;
        } else {
            short nextFileOffset = (short)(
                BinUtils.bytesToShort(FileStoreBuffer, ram[READ_BASE_DATA_PTR]) +
                BinUtils.bytesToShort(FileStoreBuffer, (short)(ram[READ_BASE_DATA_PTR] + 2)) + 4
            );

            ram[READ_BASE_DATA_PTR] += nextFileOffset;
            ram[READ_DATA_PTR] = (short)0x00;
            ram[READ_TRANSFER_SIZE] = (short)0x00;

            return (ram[READ_BASE_DATA_PTR] < writeBaseDataPtr);
        }
    }

    public void initReadTransfer(byte[] buffer, short offset) {
        Util.arrayCopy(FileStoreBuffer, ram[READ_BASE_DATA_PTR], buffer, offset, (short)4);

        ram[READ_TRANSFER_SIZE] = (short)(
            BinUtils.bytesToShort(FileStoreBuffer, ram[READ_BASE_DATA_PTR]) +
            BinUtils.bytesToShort(FileStoreBuffer, (short)(ram[READ_BASE_DATA_PTR] + 2)) + 4
        );

        ram[READ_DATA_PTR] = (short)4;
    }

    public boolean read(byte[] buffer, short offset, short size) {
        if ((short)(ram[READ_DATA_PTR] + size) > ram[READ_TRANSFER_SIZE]) {
            return false;
        } else {
            Util.arrayCopy(FileStoreBuffer, (short)(ram[READ_BASE_DATA_PTR] + ram[READ_DATA_PTR]), buffer, offset, size);
            ram[READ_DATA_PTR] += size;

            return true;
        }
    }
}