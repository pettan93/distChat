package distChat.comm;

import distChat.MyUtils;
import distChat.model.ChatUser;
import kademlia.KadConfiguration;
import kademlia.KadServer;
import kademlia.KademliaNode;
import kademlia.dht.KademliaDHT;
import kademlia.message.Message;
import kademlia.message.Receiver;
import kademlia.node.Node;

import java.io.IOException;

public class ChatRoomUpdateReciever implements Receiver {

    private final KadServer server;
    private final KademliaNode localNode;
    private final KademliaDHT dht;
    private final KadConfiguration config;
    private ChatUser me;


    public ChatRoomUpdateReciever(KadServer server, KademliaNode localNode, KademliaDHT dht, KadConfiguration config) {
        this.server = server;
        this.localNode = localNode;
        this.dht = dht;
        this.config = config;
        this.me = localNode.getChatUser();
    }

    @Override
    public void receive(Message incoming, int commId) {

        ChatRoomUpdateMessage msg = (ChatRoomUpdateMessage) incoming;
        Node origin = msg.getOrigin();


        var chatRoom = me.getInvolvedChatroomByName(msg.getContent().getChatRoomName());

        // incoming msg about succesfull joining to chatroom
        if (chatRoom == null) {

            Runnable task = () -> {
                var chatRoomToSave = me.lookupChatRoomByName(msg.getContent().getChatRoomName());
                me.updateInvolvedChatroomByName(chatRoomToSave.getName(), chatRoomToSave);

            };
            Thread thread = new Thread(task);
            thread.start();

            return;
        }

        if (origin.getNodeId().toString().equals(MyUtils.nickNameToKademliaHex(chatRoom.getOwnerId()))) {
            new ChatRoomUpdateBroadcastReciever(this.server, this.localNode, this.dht, this.config).receive(incoming, commId);
        } else {
            new ChatRoomUpdateRequestReciever(this.server, this.localNode, this.dht, this.config).receive(incoming, commId);
        }


    }

    @Override
    public void timeout(int conversationId) throws IOException {

    }
}
