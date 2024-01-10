package applet;

import javacard.framework.APDU;

public class ModuleCrypt extends Module {
    private static final byte SetKey = (byte)0x00;
    private static final byte Init = (byte)0x01;
    private static final byte Update = (byte)0x02;
    private static final byte Finalize = (byte)0x03;

    private final SecureEnclave secureEnclave;

    public ModuleCrypt(SecureEnclave secureEnclave) {
        this.secureEnclave = secureEnclave;
    }

    public void execute(APDU apdu, byte[] buffer) {
        byte state = buffer[P1];
        switch (state) {
            case SetKey: setKey(apdu, buffer); break;
            case Init: init(apdu, buffer); break;
            case Update: update(apdu, buffer); break;
            case Finalize: finalize(apdu, buffer); break;
            default: break;
        }
    }

    private void setKey(APDU apdu, byte[] buffer) {
        apdu.setIncomingAndReceive();

        short length = (short)(buffer[LC] & 0xFF);
        if (length == secureEnclave.getKeySize()) {
            secureEnclave.setKey(buffer, DATA);
        } else {
            CardStatusCodes.SendStatus(CardStatusCodes.CARD_CRYPT_ERROR);
        }
    }

    private void init(APDU apdu, byte[] buffer) {
        apdu.setIncomingAndReceive();

        secureEnclave.init(buffer[P2]);
    }

    private void update(APDU apdu, byte[] buffer) {
        apdu.setIncomingAndReceive();

        short length = (short)(buffer[LC] & 0xFF);
        length = secureEnclave.update(buffer, DATA, buffer, (short)0, length);
        if (length > 0) {
            apdu.setOutgoingAndSend((short)0, length);
        } else {
            CardStatusCodes.SendStatus(CardStatusCodes.CARD_CRYPT_ERROR);
        }
    }

    private void finalize(APDU apdu, byte[] buffer) {
        apdu.setIncomingAndReceive();

        short length = (short)(buffer[LC] & 0xFF);
        length = secureEnclave.finalize(buffer, DATA, buffer, (short)0, length);
        if (length == 0) {
            CardStatusCodes.SendStatus(CardStatusCodes.EXPECTED_PAYLOAD_EMPTY);
        } else if (length > 0) {
            apdu.setOutgoingAndSend((short)0, length);
        } else {
            CardStatusCodes.SendStatus(CardStatusCodes.CARD_CRYPT_ERROR);
        }
    }
}