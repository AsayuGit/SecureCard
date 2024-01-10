package client;

import java.io.IOException;
import java.io.FileNotFoundException;

public class ActionUploadFile extends Action {
    private final CardModuleFileWrite moduleFileWrite;

    public ActionUploadFile(CardInterface cardInterface) {
        super(cardInterface);

        // Module initialisation
        moduleFileWrite = new CardModuleFileWrite(cardInterface);
    }

    @Override
    public void execute() {
        String filename = KeyboardInterface.askUser("Please enter the name of the file to upload");

        if (filename.length() > 0) {
            try {
                // Loads the file from disk
                byte[] filedata = DiskInterface.loadFile(filename);

                System.out.println("Uploading " + filename + " ...");

                // Remove the directory from the filename
                filename = DiskInterface.trimDir(filename);

                // Then write the file to the card
                moduleFileWrite.initTransfer((short)filename.length(), (short)filedata.length);
                moduleFileWrite.sendData(filename.getBytes());
                moduleFileWrite.sendData(filedata);
                moduleFileWrite.finalizeTransfer();

                System.out.println("Upload successful !");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("No filename entered");
        }
    }
}