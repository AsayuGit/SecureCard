package applet;

import javacard.framework.JCSystem;

import javacard.security.Key;
import javacard.security.KeyBuilder;
import javacard.security.DESKey;
import javacard.security.CryptoException;
import javacardx.crypto.Cipher;

public class SecureEnclave {
    private static final byte EncMode = (byte)0x00;
    private static final byte DecMode = (byte)0x01;

    private static final byte ENGINE_NOPAD = (byte)0x00;
    private static final byte ENGINE_PAD = (byte)0x01;

    private final Key key;

    private final Cipher desEnc_NOPAD;
    private final Cipher desDec_NOPAD;

    private final Cipher desEnc_PAD;
    private final Cipher desDec_PAD;

    // Volatile memory array (Cleared on deselect)
    private final Object[] ram;

    public SecureEnclave() {
        // Initialize the transient short array
        ram = JCSystem.makeTransientObjectArray((short)2, JCSystem.CLEAR_ON_DESELECT);

        // Initialize each cipher instances
        desEnc_NOPAD = Cipher.getInstance(Cipher.ALG_DES_ECB_NOPAD, false);
        desDec_NOPAD = Cipher.getInstance(Cipher.ALG_DES_ECB_NOPAD, false);

        desEnc_PAD = Cipher.getInstance(Cipher.ALG_DES_ECB_ISO9797_M2, false);
        desDec_PAD = Cipher.getInstance(Cipher.ALG_DES_ECB_ISO9797_M2, false);

        // Initialize the Key
        key = KeyBuilder.buildKey(KeyBuilder.TYPE_DES, KeyBuilder.LENGTH_DES, false);

        setKey(new byte[8], (short)0); // Default key
    }

    public void setKey(byte[] buffer, short offset) {
        ((DESKey)key).setKey(buffer, offset);

        desEnc_NOPAD.init(key, Cipher.MODE_ENCRYPT);
        desDec_NOPAD.init(key, Cipher.MODE_DECRYPT);

        desEnc_PAD.init(key, Cipher.MODE_ENCRYPT);
        desDec_PAD.init(key, Cipher.MODE_DECRYPT);
    }

    public short getKeySize() {
        return KeyBuilder.LENGTH_DES / 8;
    }

    public void init(byte mode) {
        // Set the cipher mode (encrypt or decrypt)
        switch (mode) {
            case EncMode:
                ram[ENGINE_NOPAD] = desEnc_NOPAD;
                ram[ENGINE_PAD] = desEnc_PAD;
                break;
    
            case DecMode:
                ram[ENGINE_NOPAD] = desDec_NOPAD;
                ram[ENGINE_PAD] = desDec_PAD;
                break;

            default: break;
        }
    }

    // Cipher without padding
    public short update(byte[] srcBuffer, short srcOffset, byte[] dstBuffer, short dstOffset, short size) {
        if (ram[ENGINE_NOPAD] == null) return (short)0;
    
        return ((Cipher)(ram[ENGINE_NOPAD])).doFinal(srcBuffer, srcOffset, size, dstBuffer, dstOffset);
    }

    // Cipher with padding
    public short finalize(byte[] srcBuffer, short srcOffset, byte[] dstBuffer, short dstOffset, short size) {
        short length = 0;

        if (ram[ENGINE_PAD] != null) {
            try {
                length = ((Cipher)(ram[ENGINE_PAD])).doFinal(srcBuffer, srcOffset, size, dstBuffer, dstOffset);
            } catch (CryptoException e) {}
        }
        
        ram[ENGINE_NOPAD] = null;
        ram[ENGINE_PAD] = null;
        
        return length;
    }
}