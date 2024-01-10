package client;

public class ActionDESKeyChange extends Action {
    private final CardModuleCrypt moduleCrypt;

    public ActionDESKeyChange(CardInterface cardInterface) {
        super(cardInterface);

        // Module initialisation
        moduleCrypt = new CardModuleCrypt(cardInterface);
    }

    @Override
    public void execute() {
        System.out.println("Changing DES key ...");

        try {
            // Ask user for key
            byte[] key = KeyboardInterface.askUser("Please enter a new DES KEY", (short)0x08);
            
            // Then set the card key
            moduleCrypt.setKey(key);

            System.out.println("DES key changed successfuly !");
        } catch (CardIOException e) {
            System.out.println(e.getMessage());
        }
    }
}