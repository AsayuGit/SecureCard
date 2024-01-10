package client;

import java.io.IOException;

// Emited when the card entounter an error
public class CardIOException extends IOException {
    public CardIOException() {
        super("ERROR: Card communication failed !");
    }

    public CardIOException(String message) {
        super(message);
    }
}