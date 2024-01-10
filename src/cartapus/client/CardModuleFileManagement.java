package client;

public class CardModuleFileManagement extends CardModule {
    public CardModuleFileManagement(CardInterface cardInterface) {
        super(cardInterface);
    }

    public void rewindFiles() throws CardIOException {
        OutgoingCardData data = new OutgoingCardData();
        data.setModule(CardModuleEnum.FILE_MANAGEMENT);
        data.setMethod(CardFileManagmentMethodEnum.RewindFiles);

        try {
            IncommingCardData answer = cardInterface.sendData(data);
            if (answer.getStatusCode() != CardStatusCodeEnum.SUCCESS) {
                throw new CardIOException("ERROR: Card Empty !");
            }
        } catch (NoSuchFieldException e) {
            throw new CardIOException();
        }
    }

    public boolean nextFile() throws CardIOException {
        OutgoingCardData data = new OutgoingCardData();
        data.setModule(CardModuleEnum.FILE_MANAGEMENT);
        data.setMethod(CardFileManagmentMethodEnum.NextFile);

        try {
            IncommingCardData answer = cardInterface.sendData(data);
            return (answer.getStatusCode() == CardStatusCodeEnum.SUCCESS);
        } catch (NoSuchFieldException e) {
            throw new CardIOException();
        }
    }
}