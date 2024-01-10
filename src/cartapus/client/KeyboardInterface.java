package client;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

// The class KeyboardInterface abstract the user input
public abstract class KeyboardInterface {
    private static final BufferedReader inputBuffer = new BufferedReader(new InputStreamReader(System.in));

    public static String getUserInput() {
        try {
            return inputBuffer.readLine();
        } catch (IOException e) {
            return "";
        }
    }

    // Ask the user a question until a valid byte array of the correct length is entered
    public static byte[] askUser(String question, short length) {
        byte[] userArray = new byte[length];

        while (true) {
            try {
                String userInput = askUser(question).trim();
                if (userInput.length() != 16) continue;

                short stringIndex = 0;
                for (short i = 0; i < length; ++i) {
                    userArray[i] = (byte)Integer.parseInt(userInput.substring(stringIndex, stringIndex + 2), 16);
                    System.out.println(userArray[i]);
                    stringIndex += 2;
                } break;
            } catch (NumberFormatException e) {
                continue;
            }
        }

        return userArray;
    }

    // Ask the user a question and return the user input
    public static String askUser(String question) {
        System.out.println(question + " :");
        System.out.print("--> ");
        return KeyboardInterface.getUserInput();
    }
}