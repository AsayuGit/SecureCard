package client;

import java.io.IOException;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class ActionEncryptFile extends Action {
    private final CardModuleCrypt moduleCrypt;

    public ActionEncryptFile(CardInterface cardInterface) {
        super(cardInterface);

        // Module initialisation
        moduleCrypt = new CardModuleCrypt(cardInterface);
    }

    @Override
    public void execute() {
        // 1. ask filename to cipher
        String fileIn = KeyboardInterface.askUser("Please enter the name of the file to encrypt");

        // 2. ask output filename
        String fileOut = KeyboardInterface.askUser("Please enter the name of the file to save");

        try {
            DataInputStream plainData = null;
            DataOutputStream encryptedData = null;
            try {
                // 3. Open streams
                plainData = DiskInterface.openFileInput(fileIn);
                encryptedData = DiskInterface.openFileOutput(fileOut);

                // 4. encrypt
                System.out.println("Processing ...");
                moduleCrypt.encrypt(plainData, encryptedData);

                // 5. Profit
                System.out.println("File encrypted successfuly !");
            } finally {
                if (plainData != null) plainData.close();
                if (encryptedData != null) encryptedData.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } 
    }
}