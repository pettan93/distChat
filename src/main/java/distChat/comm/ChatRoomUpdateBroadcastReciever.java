package distChat.comm;

import distChat.model.ChatRoom;
import distChat.model.ChatUser;
import kademlia.KadConfiguration;
import kademlia.KadServer;
import kademlia.KademliaNode;
import kademlia.dht.KademliaDHT;
import kademlia.message.AcknowledgeMessage;
import kademlia.message.Message;
import kademlia.message.Receiver;
import kademlia.node.Node;

import java.io.IOException;

public class ChatRoomUpdateBroadcastReciever implements Receiver {

    private final KadServer server;
    private final KademliaNode localNode;
    private final KademliaDHT dht;
    private final KadConfiguration config;
    private ChatUser me;

    public ChatRoomUpdateBroadcastReciever(KadServer server, KademliaNode localNode, KademliaDHT dht, KadConfiguration config) {
        this.server = server;
        this.localNode = localNode;
        this.dht = dht;
        this.config = config;
        this.me = localNode.getChatUser();
    }

    @Override
    public void receive(Message incoming, int commId) {
        me.log("I get a broadcast, updating my copy");

        var msg = (ChatRoomUpdateMessage) incoming;

        var myChatroomCopy = me.getChatRoomsInvolved().get(msg.getContent().getChatRoomName());

        if (msg.getContent().getChatRoomMessage() != null) {
            myChatroomCopy.addMessage(msg.getContent().getChatRoomMessage());
        }

        if (msg.getContent().getChatRoomParticipant() != null) {
            myChatroomCopy.addParticipants(msg.getContent().getChatRoomParticipant());
        }


        me.updateInvolvedChatroomByName(myChatroomCopy.getName(), myChatroomCopy);


    }

    @Override
    public void timeout(int conversationId) throws IOException {
        me.log("MesBroadcastUpdateReciever message timeout.");
    }
}
