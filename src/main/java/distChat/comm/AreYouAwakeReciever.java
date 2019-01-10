package distChat.comm;

import distChat.model.ChatUser;
import kademlia.KademliaNode;
import kademlia.exceptions.RoutingException;
import kademlia.message.AcknowledgeMessage;
import kademlia.message.Message;
import kademlia.message.Receiver;
import kademlia.operation.Operation;

import java.io.IOException;

public class AreYouAwakeReciever implements Receiver {

    private final KademliaNode localNode;
    private ChatUser me;

    public AreYouAwakeReciever(KademliaNode localNode) {
        this.localNode = localNode;
        this.me = localNode.getChatUser();
    }

    @Override
    public void receive(Message incoming, int commId) {
//        me.log("I get a 'are you awake' msg, and yes, I am");

        var msg = (AreYouAwakeMessage) incoming;


        if (this.me.getKadNode().getServer().isRunning())
        {
            /* Say "yeah, I am awake" */
            try {
                this.me.getKadNode().getServer().reply(msg.getOrigin(), new AcknowledgeMessage(localNode.getNode()), commId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void timeout(int conversationId) throws IOException {
        me.log("AreYouAwakeReciever message timeout.");
    }


}
