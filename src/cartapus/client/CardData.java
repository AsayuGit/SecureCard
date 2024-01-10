package client;

import opencard.core.util.HexString;

public abstract class CardData {
    public abstract byte[] getBytes();

    @Override
    public final String toString() {
        return HexString.hexify(getBytes()).replace('\n', ' ');
    }
}