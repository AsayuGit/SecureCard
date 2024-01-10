package applet;

import javacard.framework.APDU;

public class ModuleFileWrite extends Module {
    private static final byte SetDataSize = (byte)0x00;
    private static final byte WriteData = (byte)0x01;
    private static final byte Finalize = (byte)0x02;

    private final FileStore fileStore;

    public ModuleFileWrite(FileStore fileStore) {
        this.fileStore = fileStore;
    }

    public void execute(APDU apdu, byte[] buffer) {
        byte state = buffer[P1];
        switch (state) {
            case SetDataSize: setDataSize(apdu, buffer); break;
            case WriteData: writeData(apdu, buffer); break;
            case Finalize: finalizeTransfer(); break;
            default: break;
        }
    }

    private void setDataSize(APDU apdu, byte[] buffer) {
        apdu.setIncomingAndReceive();

        short nameSize = BinUtils.bytesToShort(buffer, (short)DATA);
        short dataSize = BinUtils.bytesToShort(buffer, (short)(DATA + 2));

        if (!fileStore.initWriteTransfer(nameSize, dataSize)) {
            CardStatusCodes.SendStatus(CardStatusCodes.CARD_FULL);
        }
    }

    private void writeData(APDU apdu, byte[] buffer) {
        apdu.setIncomingAndReceive();

        short length = (short)(buffer[LC] & 0xFF);
        if (!fileStore.write(buffer, DATA, length)) {
            CardStatusCodes.SendStatus(CardStatusCodes.WRITE_ERROR);
        }
    }

    private void finalizeTransfer() {
        if (!fileStore.closeTranfer()) {
            CardStatusCodes.SendStatus(CardStatusCodes.FINALIZE_ERROR);
        }
    }
}