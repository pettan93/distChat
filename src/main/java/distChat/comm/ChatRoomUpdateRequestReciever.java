package distChat.comm;

import distChat.MyUtils;
import distChat.model.ChatRoom;
import distChat.model.ChatRoomParticipant;
import distChat.model.ChatUser;
import distChat.operation.OwnerChatroomUpdateOperation;
import kademlia.KadConfiguration;
import kademlia.KadServer;
import kademlia.KademliaNode;
import kademlia.dht.KademliaDHT;
import kademlia.message.AcknowledgeMessage;
import kademlia.message.Message;
import kademlia.message.Receiver;
import kademlia.node.Node;

import java.io.IOException;

public class ChatRoomUpdateRequestReciever implements Receiver {

    private final KademliaNode localNode;
    private ChatUser me;

    public ChatRoomUpdateRequestReciever(KademliaNode localNode) {
        this.localNode = localNode;
        this.me = localNode.getChatUser();
    }

    @Override
    public void receive(Message incoming, int commId) {
        me.log("Comm[" + commId + "] ChatRoomUpdateRequestReciever processing " + incoming);

        ChatRoomUpdateMessage msg = (ChatRoomUpdateMessage) incoming;


        Runnable task = () -> {

            // get chatroom from storagers
            ChatRoom targetChatRoom = me.lookupChatRoomByName(msg.getContent().getChatRoomName());

            // if I am owner
            if (targetChatRoom.getOwnerId().equals(me.getNickName())) {

                // Reply with confiromation
                if (me.getKadNode().getServer().isRunning()) {
                    Message reply = new AcknowledgeMessage(localNode.getNode());
                    try {
                        me.getKadNode().getServer().reply(msg.getOrigin(), reply, commId);
                        me.log("Comm[" + commId + "] Confirmation about recieving chatroomUpdateMessage sent!");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                // process update & broadcast
                me.getOp().queueOperation(new OwnerChatroomUpdateOperation(me, targetChatRoom.getName(), msg, commId));


            } else {
                // i am not owner
                me.log("Comm[" + commId + "] I recieved chatrom update request but I am not owner");
            }

        };
        new Thread(task).start();


    }

    @Override
    public void timeout(int conversationId) throws IOException {
        me.log("MesReqUpdateReciever message timeout.");
    }
}
