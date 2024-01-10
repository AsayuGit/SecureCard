package client;

import java.io.IOException;
import java.io.FileNotFoundException;

public class ActionDownloadFile extends Action {
    private final CardModuleFileManagement moduleFileManagement;
    private final CardModuleFileRead moduleFileRead;

    public ActionDownloadFile(CardInterface cardInterface) {
        super(cardInterface);

        // Module Initialisation
        moduleFileManagement = new CardModuleFileManagement(cardInterface);
        moduleFileRead = new CardModuleFileRead(cardInterface);
    }

    @Override
    public void execute() {
        String filename = KeyboardInterface.askUser("Please enter the name or ID of the file you with to download");

        if (filename.length() > 0) {
            try {
                short dataSize = 0;
                try {
                    // try to interpret the filename as an index and get the data size
                    dataSize = selectFile(Integer.parseInt(filename));
                    System.out.println("Downloading file [ " + filename + " ] ...");
                } catch (NumberFormatException e) {
                    // If not then try to interpret it as a filename and get the data size
                    dataSize = selectFile(filename);
                    System.out.println("Downloading " + filename + " ...");
                }

                // Then read the filedata from the card
                byte[] fileData = moduleFileRead.readData(dataSize);

                // Finally prompt the user where to save the card
                filename = KeyboardInterface.askUser("Please enter the name of the file to save");
                if (filename.length() > 0) {
                    DiskInterface.saveFile(filename, fileData);
                    System.out.println("File saved successfuly !");
                } else {
                    System.out.println("File discarded");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("No filename entered");
        }
    }

    private short selectFile(String filename) throws FileNotFoundException, CardIOException {
        moduleFileManagement.rewindFiles();

        // Get each filename and check if they match the requested filename
        do {
            short[] fileProperties = moduleFileRead.initTransfer();
            String currentFilename = new String(moduleFileRead.readData(fileProperties[0]));
            if (currentFilename.equals(filename)) {
                return fileProperties[1];
            }
        } while (moduleFileManagement.nextFile());

        throw new FileNotFoundException(filename + " is not present on card !");
    }

    private short selectFile(int fileID) throws FileNotFoundException, CardIOException {
        moduleFileManagement.rewindFiles();

        // Skip as many file as the fileID
        int i = 0;
        do {
            short[] fileProperties = moduleFileRead.initTransfer();
            moduleFileRead.readData(fileProperties[0]);
            if (i == fileID) {
                return fileProperties[1];
            }
            ++i;
        } while (moduleFileManagement.nextFile());

        throw new FileNotFoundException("File id [ " + fileID + " ] is not present on card !");
    }
}