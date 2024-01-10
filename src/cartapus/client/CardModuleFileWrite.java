package client;

import java.io.IOException;
import java.util.Arrays;

public class CardModuleFileWrite extends CardModule {
    public CardModuleFileWrite(CardInterface cardInterface) {
        super(cardInterface);
    }

    // Initialize a write transfer (send the size of the data to be written)
    public void initTransfer(short nameSize, short dataSize) throws CardIOException {
        OutgoingCardData data = new OutgoingCardData();

        data.setModule(CardModuleEnum.FILE_WRITE);
        data.setMethod(CardFileWriteMethodEnum.TransferInit);
        data.setData(new byte[] {
            (byte)(nameSize >> 8), (byte)(nameSize & 0xFF), 
            (byte)(dataSize >> 8), (byte)(dataSize & 0xFF)
        });

        try {
            IncommingCardData answer = cardInterface.sendData(data);
            if (answer.getStatusCode() != CardStatusCodeEnum.SUCCESS) {
                throw new CardIOException("ERROR: No space left on card !");
            }
        } catch (NoSuchFieldException e) {
            throw new CardIOException();
        }
    }

    // Write a buffer to the card
    public void sendData(byte[] fileData) throws CardIOException {
        short blockSize = maxTransferSize;

        OutgoingCardData data = new OutgoingCardData();
        data.setModule(CardModuleEnum.FILE_WRITE);
        data.setMethod(CardFileWriteMethodEnum.WriteData);

        for (int byteIndex = 0; byteIndex < fileData.length; byteIndex += blockSize) {
            if ((fileData.length - byteIndex) < blockSize) {
                blockSize = (short)(fileData.length - byteIndex);
            }

            data.setData(Arrays.copyOfRange(fileData, byteIndex, byteIndex + blockSize));

            try {
                IncommingCardData answer = cardInterface.sendData(data);
                CardStatusCodeEnum statusCode = answer.getStatusCode();
                if ((statusCode == CardStatusCodeEnum.CARD_ERROR) && (answer.getStatusData() == 0x02)) {
                    throw new CardIOException("ERROR: Card full !");
                } if (statusCode != CardStatusCodeEnum.SUCCESS) {
                    throw new CardIOException("ERROR: Couldn't write on card !");
                }
            } catch (NoSuchFieldException e) {
                throw new CardIOException();
            }
        }
    }

    // Then finalize the file transfer to commit the file to the card
    public void finalizeTransfer() throws CardIOException {
        OutgoingCardData data = new OutgoingCardData();
        data.setModule(CardModuleEnum.FILE_WRITE);
        data.setMethod(CardFileWriteMethodEnum.Finalize);

        try {
            IncommingCardData answer = cardInterface.sendData(data);
            if (answer.getStatusCode() != CardStatusCodeEnum.SUCCESS) {
                throw new CardIOException("ERROR: Couldn't finalise file transfer !");
            }
        } catch (NoSuchFieldException e) {
            throw new CardIOException();
        }
    }
}