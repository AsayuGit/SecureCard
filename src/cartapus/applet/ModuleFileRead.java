package applet;

import javacard.framework.APDU;

public class ModuleFileRead extends Module {
    private static final byte GetDataSize = (byte)0x00;
    private static final byte ReadData = (byte)0x01;

    private final FileStore fileStore;

    public ModuleFileRead(FileStore fileStore) {
        this.fileStore = fileStore;
    }

    public void execute(APDU apdu, byte[] buffer) {
        byte state = buffer[P1];
        switch (state) {
            case GetDataSize: getDataSize(apdu, buffer); break;
            case ReadData: readData(apdu, buffer); break;
            default: break;
        }
    }

    private void getDataSize(APDU apdu, byte[] buffer) {
        apdu.setIncomingAndReceive();

        fileStore.initReadTransfer(buffer, (short)0);
        apdu.setOutgoingAndSend((short)0, (short)4);
    }

    private void readData(APDU apdu, byte[] buffer) {
        apdu.setIncomingAndReceive();

        short length = BinUtils.bytesToShort(buffer, DATA);
        if (!fileStore.read(buffer, (short)0, length)) {
            CardStatusCodes.SendStatus(CardStatusCodes.READ_ERROR);
        } else {
            apdu.setOutgoingAndSend((short)0, length);
        }
    }
}