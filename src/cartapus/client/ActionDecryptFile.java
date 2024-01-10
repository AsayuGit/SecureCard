package client;

import java.io.IOException;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class ActionDecryptFile extends Action {
    private final CardModuleCrypt moduleCrypt;

    public ActionDecryptFile(CardInterface cardInterface) {
        super(cardInterface);

        // Module initialisation
        moduleCrypt = new CardModuleCrypt(cardInterface);
    }

    @Override
    public void execute() {
        // 1. ask filename to cipher
        String fileIn = KeyboardInterface.askUser("Please enter the name of the file to decrypt");

        // 2. ask output filename
        String fileOut = KeyboardInterface.askUser("Please enter the name of the file to save");

        try {
            DataInputStream encryptedData = null;
            DataOutputStream plainData = null;
            try {
                // 3. Open Streams
                encryptedData = DiskInterface.openFileInput(fileIn);
                plainData = DiskInterface.openFileOutput(fileOut);
                

                // 4. decrypt
                System.out.println("Processing ...");
                moduleCrypt.decrypt(encryptedData, plainData);

                // 5. Profit
                System.out.println("File decrypted successfuly !");
            } finally {
                if (encryptedData != null) encryptedData.close();
                if (plainData != null) plainData.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}