package client;

// The action base class
public abstract class Action {
    protected final CardInterface cardInterface;

    public Action(CardInterface cardInterface) {
        this.cardInterface = cardInterface;
    }

    // Execute the action
    public abstract void execute();
}