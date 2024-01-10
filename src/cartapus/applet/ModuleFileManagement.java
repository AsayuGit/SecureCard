package applet;

import javacard.framework.APDU;

public class ModuleFileManagement extends Module {
    private static final byte RewindFiles = (byte)0x00;
    private static final byte NextFile = (byte)0x01;

    private final FileStore fileStore;

    public ModuleFileManagement(FileStore fileStore) {
        this.fileStore = fileStore;
    }

    public void execute(APDU apdu, byte[] buffer) {
        byte state = buffer[P1];
        switch (state) {
            case RewindFiles: rewindFiles(); break;
            case NextFile: nextFile(); break;
            default: break;
        }
    }

    private void rewindFiles() {
        if (!fileStore.rewindFiles()) {
            CardStatusCodes.SendStatus(CardStatusCodes.CARD_EMPTY);
        }
    }

    private void nextFile() {
        if (!fileStore.nextFile()) {
            CardStatusCodes.SendStatus(CardStatusCodes.NO_MORE_FILES);
        }
    }
}