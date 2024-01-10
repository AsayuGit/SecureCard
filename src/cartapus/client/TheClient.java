package client;

import java.io.IOException;

public class TheClient {
    public static void main(String[] args) throws InterruptedException {
        CardInterface javaCard = null;

        try {
            // Initialize the JavaCard
            javaCard = new CardInterface();

            // Initialize the Main menu object
            Menu mainMenu = new Menu();

            // Add each menu item to the main menu
            mainMenu.addMenuItem(new MenuItem("Upload file", new ActionUploadFile(javaCard)));
            mainMenu.addMenuItem(new MenuItem("List files", new ActionListFiles(javaCard)));
            mainMenu.addMenuItem(new MenuItem("Download file", new ActionDownloadFile(javaCard)));
            mainMenu.addMenuItem(new MenuItem("Encrypt file", new ActionEncryptFile(javaCard)));
            mainMenu.addMenuItem(new MenuItem("Decrypt file", new ActionDecryptFile(javaCard)));
            mainMenu.addMenuItem(new MenuItem("Change DES key", new ActionDESKeyChange(javaCard)));

            mainMenu.addMenuItem(new MenuItem("Verbose Toogle", new ActionOptionMenu(javaCard)));

            // Then launch the main menu
            mainMenu.display();

            // When done eject the JavaCard
            javaCard.eject();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
