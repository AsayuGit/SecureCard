package client;

import java.util.Arrays;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class CardModuleCrypt extends CardModule {
    public CardModuleCrypt(CardInterface cardInterface) {
        super(cardInterface);

        maxTransferSize = 248; // Upper block allied boundary
    }

    public void setKey(byte[] key) throws CardIOException {
        OutgoingCardData data = new OutgoingCardData();
        data.setModule(CardModuleEnum.CRYPT);
        data.setMethod(CardCryptMethodEnum.SetKey);
        data.setData(key);

        try {
            IncommingCardData answer = cardInterface.sendData(data);
            if (answer.getStatusCode() != CardStatusCodeEnum.SUCCESS) {
                throw new CardIOException("ERROR: Can't set DES key !");
            }
        } catch (NoSuchFieldException e) {
            throw new CardIOException();
        }
    }

    private void init(byte mode) throws CardIOException {
        OutgoingCardData data = new OutgoingCardData();
        data.setModule(CardModuleEnum.CRYPT);
        data.setMethod(CardCryptMethodEnum.Init);
        data.setParameter(mode);

        try {
            IncommingCardData answer = cardInterface.sendData(data);
            if (answer.getStatusCode() != CardStatusCodeEnum.SUCCESS) {
                throw new CardIOException("ERROR: Can't initialize DES engine !");
            }
        } catch (NoSuchFieldException e) {
            throw new CardIOException();
        }
    }

    public void encrypt(DataInputStream input, DataOutputStream output) throws IOException {
        init((byte)0);
        cipher(input, output);
    }

    public void decrypt(DataInputStream input, DataOutputStream output) throws IOException {
        init((byte)1);
        cipher(input, output);
    }

    private void cipher(DataInputStream input, DataOutputStream output) throws IOException {
        byte[] buffer = new byte[maxTransferSize];

        OutgoingCardData data = new OutgoingCardData();
        data.setModule(CardModuleEnum.CRYPT);
        data.setExpectedSize((byte)maxTransferSize);

        // Process Data
        while (input.available() > 0) {
            int read = input.read(buffer);
            data.setData(Arrays.copyOfRange(buffer, 0, read));

            // Update the cipher until the last block of data then finalize the last block (padding)
            if (input.available() > 0) {
                data.setMethod(CardCryptMethodEnum.Update);
            } else {
                data.setMethod(CardCryptMethodEnum.Finalize);
            }

            try {
                // Send the data to the card and get the answer
                IncommingCardData answer = cardInterface.sendData(data);

                // If the card returns more data then get it as well
                CardStatusCodeEnum statusCode = answer.getStatusCode();
                while (statusCode == CardStatusCodeEnum.DATA_AVAILABLE) {
                    output.write(answer.getData());
                    answer = cardInterface.getResponse((byte)answer.getStatusData());
                    statusCode = answer.getStatusCode();
                }

                // Finaly if successful then write the data to the output file
                if ((statusCode == CardStatusCodeEnum.SUCCESS) || ((statusCode == CardStatusCodeEnum.CARD_ERROR) && (answer.getStatusData() == 0x08))) {
                    output.write(answer.getData());
                } else {
                    throw new CardIOException("ERROR: Can't process block !");
                }
            } catch (NoSuchFieldException e) {
                throw new CardIOException();
            }
        }
    }
}