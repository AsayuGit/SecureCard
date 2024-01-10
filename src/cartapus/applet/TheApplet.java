package applet;

import javacard.framework.Applet;
import javacard.framework.APDU;
import javacard.framework.ISO7816;
import javacard.framework.ISOException;

public class TheApplet extends Applet {
    // Methods
    private static final byte FILE_MANAGEMENT = (byte)0x01;
    private static final byte FILE_WRITE      = (byte)0x02;
    private static final byte FILE_READ       = (byte)0x03;
    private static final byte CRYPT           = (byte)0x04;

    private final SecureEnclave secureEnclave;
    private final FileStore fileStore;

    private final ModuleFileManagement moduleFileManagement;
    private final ModuleFileWrite moduleFileWrite;
    private final ModuleFileRead moduleFileRead;
    private final ModuleCrypt moduleCrypt;

    protected TheApplet() {
        secureEnclave = new SecureEnclave();
        fileStore = new FileStore((short)31744); // 31 Ko

        // Init Modules
        moduleFileManagement = new ModuleFileManagement(fileStore);
        moduleFileWrite = new ModuleFileWrite(fileStore);
        moduleFileRead = new ModuleFileRead(fileStore);
        moduleCrypt = new ModuleCrypt(secureEnclave);

        this.register();
    }

    public static void install(byte[] bArray, short bOffset, byte bLength) throws ISOException {
        new TheApplet();
    } 

    public boolean select() {
        return true;
    }

    public void deselect() {
        
    }

    public void process(APDU apdu) throws ISOException {
        if (selectingApplet() == true)
            return;

        byte[] buffer = apdu.getBuffer();
        switch (buffer[1]) {
            case FILE_MANAGEMENT: moduleFileManagement.execute(apdu, buffer); break;
            case FILE_WRITE: moduleFileWrite.execute(apdu, buffer); break;
            case FILE_READ: moduleFileRead.execute(apdu, buffer); break;
            case CRYPT: moduleCrypt.execute(apdu, buffer); break;
            default: ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
        }
    }
}
