package client;

import opencard.core.service.SmartCard;
import opencard.core.service.CardRequest;
import opencard.core.terminal.CommandAPDU;
import opencard.core.terminal.ResponseAPDU;
import opencard.core.util.HexString;
import opencard.opt.util.PassThruCardService;

// Abstract the card interface and provide methods to interact with the card
public class CardInterface {
    private PassThruCardService servClient;
    private boolean verbose;

    public CardInterface() throws CardIOException {
        try {
            SmartCard.start();
            System.out.print("Smartcard inserted?... "); 

            CardRequest cr = new CardRequest(CardRequest.ANYCARD,null,null); 
            SmartCard sm = SmartCard.waitForCard(cr);

            if (sm != null) {
                System.out.println ("got a SmartCard object!\n");
            } else {
                System.out.println( "did not get a SmartCard object!\n" );
            }

            initNewCard(sm);
        } catch(Exception e) {
            throw new CardIOException("ERROR: " + e.getMessage());
        }
    }

    private void initNewCard(SmartCard card) {
        if(card != null)
            System.out.println( "Smartcard inserted\n" );
        else {
            System.out.println( "Did not get a smartcard" );
            System.exit(-1);
        }

        System.out.println("ATR: " + HexString.hexify(card.getCardID().getATR()) + "\n");

        try {
            servClient = (PassThruCardService)card.getCardService(PassThruCardService.class, true);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Applet selecting...");
        if(!selectApplet()) {
            System.out.println("Wrong card, no applet to select!\n");
            System.exit( 1 );
            return;
        } else {
            System.out.println("Applet selected");
        }
    }

    private boolean selectApplet() {
        boolean cardOk = false;
        try {
            OutgoingCardData cmd = new OutgoingCardData(new byte[] {
                (byte)0x00, (byte)0xA4, (byte)0x04, (byte)0x00, (byte)0x0A,
                    (byte)0xA0, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x62, 
                    (byte)0x03, (byte)0x01, (byte)0x0C, (byte)0x06, (byte)0x01
            });
            IncommingCardData resp = sendData(cmd);
            if (resp.toString().equals("90 00")) {
                cardOk = true;
            }
        } catch(Exception e) {
            System.out.println( "Exception caught in selectApplet: " + e.getMessage() );
            java.lang.System.exit( -1 );
        }
        return cardOk;
    }

    public boolean toggleVerbose() {
        return (verbose = !verbose);
    }

    public void eject() {
        try {
            SmartCard.shutdown();
        } catch (Exception e) {
            System.out.println("Eject error: " + e.getMessage());
        }
    }

    // Send an APDU to the javacard
    public IncommingCardData sendData(OutgoingCardData cardData) {
        if (verbose) System.out.println( "--> Term: " + cardData);
        return sendRaw(cardData.getBytes());
    }

    // Obtain the rest of the data from the javacard (in case of status 0x61XX)
    public IncommingCardData getResponse(short length) {
        if (verbose) System.out.println( "--> Term: GET_RESPONSE");
        return sendRaw(new byte[] {(byte)0x00, (byte)0xC0, (byte)0x00, (byte)0x00, (byte)length});
    }

    private IncommingCardData sendRaw(byte[] buffer) {
        IncommingCardData answer = null;

        try {
            CommandAPDU cmd = new CommandAPDU(buffer);
            ResponseAPDU result = this.servClient.sendCommandAPDU(cmd);

            answer = new IncommingCardData(result.getBytes());
            if (verbose) System.out.println( "<-- Card: " + answer);
        } catch( Exception e ) {
            System.out.println( "Exception caught in send: " + e.getMessage() );
            java.lang.System.exit(-1);
        }

        return answer;
    }
}