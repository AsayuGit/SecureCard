package client;

import java.io.IOException;

public class ActionListFiles extends Action {
    private final CardModuleFileManagement moduleFileManagement;
    private final CardModuleFileRead moduleFileRead;

    public ActionListFiles(CardInterface cardInterface) {
        super(cardInterface);

        // Module initialisation
        moduleFileManagement = new CardModuleFileManagement(cardInterface);
        moduleFileRead = new CardModuleFileRead(cardInterface);
    }

    @Override
    public void execute() {
        System.out.println("Listing Files ...");

        try {
            // Place the card on the first file
            moduleFileManagement.rewindFiles();
            int index = 0;

            // Then for each file
            do {
                // Get the file size
                short[] fileProperties = moduleFileRead.initTransfer();

                // Then print its filename
                String filename = new String(moduleFileRead.readData(fileProperties[0])); // Name
                System.out.println("[ " + index + " ]: " + filename + " " + BinUtils.toHumanReadable(fileProperties[1]) + " [ " + fileProperties[1] + " ]");
                
                // Finally go to the next file
                ++index;
            } while (moduleFileManagement.nextFile());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}