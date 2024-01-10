package client;

import java.io.IOException;

public class CardModuleFileRead extends CardModule {
    public CardModuleFileRead(CardInterface cardInterface) {
        super(cardInterface);
    }

    // Initialize a read transfer (and return the size of the data to be read)
    public short[] initTransfer() throws CardIOException {
        OutgoingCardData data = new OutgoingCardData();
        data.setModule(CardModuleEnum.FILE_READ);
        data.setMethod(CardFileReadMethodEnum.TransferInit);
        data.setExpectedSize((byte)4);

        try {
            IncommingCardData answer = cardInterface.sendData(data);
            if ((answer.getStatusCode() != CardStatusCodeEnum.SUCCESS) && (answer.getStatusCode() != CardStatusCodeEnum.DATA_AVAILABLE)) {
                throw new CardIOException("ERROR: Couldn't list files !");
            } else {
                try {
                    byte[] fileSize = answer.getData();
                    return new short[] {
                        BinUtils.bytesToShort(fileSize, (short)0),
                        BinUtils.bytesToShort(fileSize, (short)2)
                    };
                } catch (IndexOutOfBoundsException e) {
                    throw new CardIOException("ERROR: Couldn't list files !");
                }
            }
        } catch (NoSuchFieldException e) {
            throw new CardIOException();
        }
    }

    // Read size byte of data from the card
    public byte[] readData(short size) throws CardIOException {
        byte[] fileData = new byte[size];
        byte[] sizeData = new byte[2];

        short blockSize = maxTransferSize;

        OutgoingCardData data = new OutgoingCardData();
        data.setModule(CardModuleEnum.FILE_READ);
        data.setMethod(CardFileReadMethodEnum.ReadData);

        for (int byteIndex = 0; byteIndex < fileData.length;) {
            if ((fileData.length - byteIndex) < blockSize) {
                blockSize = (short)(fileData.length - byteIndex);
            }

            BinUtils.shortToBytes(blockSize, sizeData, (short)0);
            data.setData(sizeData);
            data.setExpectedSize((byte)blockSize);

            try {
                IncommingCardData answer = cardInterface.sendData(data);

                CardStatusCodeEnum statusCode = answer.getStatusCode();
                while (statusCode == CardStatusCodeEnum.DATA_AVAILABLE) {
                    byte[] answerData = answer.getData();
                    System.arraycopy(answerData, 0, fileData, byteIndex, answerData.length);
                    byteIndex += answerData.length;

                    answer = cardInterface.getResponse((byte)answer.getStatusData());
                    statusCode = answer.getStatusCode();
                }

                if ((statusCode != CardStatusCodeEnum.SUCCESS) && (statusCode != CardStatusCodeEnum.DATA_AVAILABLE)) {
                    throw new CardIOException("ERROR: Couldn't read from card !");
                } else {
                    byte[] answerData = answer.getData();
                    System.arraycopy(answerData, 0, fileData, byteIndex, answerData.length);
                    byteIndex += answerData.length;
                }
            } catch (NoSuchFieldException e) {
                throw new CardIOException();
            }
        }

        return fileData;
    }
}