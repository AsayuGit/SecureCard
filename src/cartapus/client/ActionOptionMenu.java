package client;

public class ActionOptionMenu extends Action {
    public ActionOptionMenu(CardInterface cardInterface) {
        super(cardInterface);
    }

    // Toggle on and off the cardInterface's verbose mode
    @Override
    public void execute() {
        System.out.println("Verbose mode " + (cardInterface.toggleVerbose() ? "on" : "off"));
    }
}