package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;

public class Menu {
    private final ArrayList<MenuItem> menuItems;

    public Menu() {
        menuItems = new ArrayList<MenuItem>();
    }

    public void addMenuItem(MenuItem item) {
        menuItems.add(item);
    }

    public void display() {
        int userInput = 0;

        // Menu main loop
        do {
            printMenu();

            // Get the user input and ensure it is an integer
            try {
                userInput = Integer.parseInt(KeyboardInterface.getUserInput());
            } catch (NumberFormatException e) {
                userInput = -1;
            }

            // Finaly if the user input is in the selectable range
            if ((userInput > 0) && (userInput <= menuItems.size())) {
                // Select the corresponding menu item
                menuItems.get(userInput - 1).select();
            }
        } while (userInput != 0);
    }

    private void printMenu() {
        System.out.println( "" );

        // Print each menu item
        for (int i = menuItems.size(); i > 0; --i) {
            System.out.println(i + ": " + menuItems.get(i - 1));
        }

        System.out.println( "0: Exit" );
        System.out.print( "--> " );
    }
}