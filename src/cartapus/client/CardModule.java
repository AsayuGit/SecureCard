package client;

// The CardModule base class ensure each CardModule optains a cardInterface instance
public abstract class CardModule {
    protected final CardInterface cardInterface;
    protected short maxTransferSize = 255;

    public CardModule(CardInterface cardInterface) {
        this.cardInterface = cardInterface;
    }
}