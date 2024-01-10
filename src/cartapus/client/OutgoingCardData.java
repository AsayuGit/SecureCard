package client;

// The OutgoingCardData is a factory class to craft Command APDUs
public class OutgoingCardData extends CardData {
    private static final byte dataMinSize = (byte)0x02;

    private static final byte CLA = (byte)0x00;
    private static final byte INS = (byte)0x01;
    private static final byte P1 = (byte)0x02;
    private static final byte P2 = (byte)0x03;

    private short LC = (byte)0x00;
    private short LE = (byte)0x00;

    private final byte[] header = new byte[4];
    private byte[] data = new byte[0];

    public OutgoingCardData() {}
    public OutgoingCardData(byte[] data) {
        System.arraycopy(data, 0, header, 0, 4);

        LC = data[4];
        if (LC > 0) {
            this.data = new byte[LC];
            System.arraycopy(data, 5, this.data, 0, LC);
        }
        if (5 + LC < data.length) {
            LE = data[5 + LC];
        }
    }

    public void setModule(CardModuleEnum module) { header[INS] = module.module; }
    public void setMethod(ICardMethodEnum method) { header[P1] = method.getMethod(); }
    public void setParameter(byte value) { header[P2] = value; }
    public void setExpectedSize(byte value) {
        // If we set an expected size yet no data is provided (or too small)
        if (data.length < dataMinSize) {
            // Then initialize a dummy data field
            LC = dataMinSize;
            data = new byte[dataMinSize];
        }
        LE = value;
    }

    public void setData(byte[] data) {
        if ((data == null) || (data.length == 0)) {
            LC = dataMinSize;
            this.data = new byte[dataMinSize];
        } else {
            LC = (short)data.length;
            if (LC < dataMinSize) LC = dataMinSize;

            this.data = new byte[LC];
            System.arraycopy(data, 0, this.data, 0, data.length);
        }
    }

    // Build the final APDU byte array
    @Override
    public byte[] getBytes() {
        // Figure out the data size
        int dataSize = 5 + LC;
        if (LE > 0) ++dataSize;

        // Allocate new array
        byte[] cardData = new byte[dataSize];

        // Set the header
        System.arraycopy(header, 0, cardData, 0, 4);

        // Copy the data over if any
        if (LC > 0) {
            cardData[4] = (byte)LC;
            System.arraycopy(data, 0, cardData, 5, LC);
        }

        // Set the expected length if any
        if (LE > 0) cardData[dataSize - 1] = (byte)LE;

        return cardData;
    }
}