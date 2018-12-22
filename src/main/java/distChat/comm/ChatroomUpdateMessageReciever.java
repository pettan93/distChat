package distChat.comm;

import kademlia.message.Message;
import kademlia.message.Receiver;

import java.io.IOException;

public class ChatroomUpdateMessageReciever implements Receiver {


    @Override
    public void receive(Message incoming, int commId)
    {
        System.out.println("Chatroom update messaage!! " + incoming);
    }

    @Override
    public void timeout(int conversationId) throws IOException
    {
        // dont care about response to response
    }
}
