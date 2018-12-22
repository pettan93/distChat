package distChat.comm;

import kademlia.KadServer;
import kademlia.message.ContentLookupMessage;
import kademlia.message.ContentMessage;
import kademlia.message.Message;
import kademlia.message.Receiver;

import java.io.IOException;

public class StoreMsgReqReciever implements Receiver {


    @Override
    public void receive(Message incoming, int commId)
    {

        System.out.println("Recieved storeMsgRequest: " + incoming);

        StoreMsgReqMessage msg = (StoreMsgReqMessage) incoming;


        // todo overit chatrmisnost, ulozit, udoslat broadcast

        ChatRoomUpdateMessage broadcast = new ChatRoomUpdateMessage();



    }

    @Override
    public void timeout(int conversationId) throws IOException
    {
        System.out.println("StoreMsgReciever message timeout.");
    }
}
