package client;

import java.util.Arrays;

// The IncommingCardData parses the data from the card into a sensible class
public class IncommingCardData extends CardData {
    protected byte[] data;

    public IncommingCardData() {
        data = new byte[] {(byte)0x90, (byte)0x00};
    }

    public IncommingCardData(byte[] data) {
        this.data = new byte[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);
    }

    public byte[] getData() {
        byte[] returnData = new byte[0];

        if (data.length > 2) {
            returnData = Arrays.copyOfRange(data, 0, data.length - 2);
        }

        return returnData;
    }

    // Return the incomming APDU's status code from the CardStatusCodeEnum
    public CardStatusCodeEnum getStatusCode() throws NoSuchFieldException {
        return CardStatusCodeEnum.get(data[data.length - 2]);
    }

    // Return the incomming APDU's data if any
    public short getStatusData() {
        return data[data.length - 1];
    }

    @Override
    public byte[] getBytes() {
        return data;
    }
}