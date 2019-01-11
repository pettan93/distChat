package distChat.comm;

import distChat.model.ChatUser;
import kademlia.KademliaNode;
import kademlia.message.AcknowledgeMessage;
import kademlia.message.Message;
import kademlia.message.Receiver;

import java.io.IOException;

public class WhoAreYouReciever implements Receiver {

    private final KademliaNode localNode;
    private ChatUser me;

    public WhoAreYouReciever(KademliaNode localNode) {
        this.localNode = localNode;
        this.me = localNode.getChatUser();
    }

    @Override
    public void receive(Message incoming, int commId) {
        me.log("Comm[" + commId + "] I get 'who are you' message'");

        var msg = (WhoAreYouMessage) incoming;



        if (me.getKadNode().getServer().isRunning()) {
            Message reply = new MyNameIsMessage(localNode.getNode(), me.getNickName());
            System.out.println(reply);
            try {
                me.getKadNode().getServer().reply(msg.getOrigin(), reply, commId);
                me.log("Comm[" + commId + "] Responding");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void timeout(int conversationId) throws IOException {
        me.log("WhoAreYouReciever message timeout.");
    }
}
