package distChat.comm;

import distChat.model.ChatRoom;
import distChat.model.ChatRoomParticipant;
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

public class NewMsgReqReciever implements Receiver {

    private final KadServer server;
    private final KademliaNode localNode;
    private final KademliaDHT dht;
    private final KadConfiguration config;
    private ChatUser me;

    public NewMsgReqReciever(KadServer server, KademliaNode localNode, KademliaDHT dht, KadConfiguration config) {
        this.server = server;
        this.localNode = localNode;
        this.dht = dht;
        this.config = config;
        this.me = localNode.getChatUser();
    }

    @Override
    public void receive(Message incoming, int commId) {
        me.log("Recieved storeMsgRequest: " + incoming);

        NewMsgReqMessage msg = (NewMsgReqMessage) incoming;
        Node origin = msg.getOrigin();





        var targetChatroomName = msg.getContent().getChatroomName();

        Runnable task = () -> {

            ChatRoom targetChatRoom = me.lookupChatRoomByName(targetChatroomName);
            me.log("lookuped chatroom to store message");

            // if I am owner
            if(targetChatRoom.getOwnerId().equals(me.getNickName())){

                me.log("jsem chatowner, storing chatroom with new message");

                var originParticipant = new ChatRoomParticipant(origin.getNodeId().toString(),msg.getContent().getSenderName());
                if(!targetChatRoom.getParticipants().contains(originParticipant)){
                    targetChatRoom.getParticipants().add(originParticipant);
                }

                targetChatRoom.addMessage(msg.getContent());

                me.storeChatroom(targetChatRoom);

                // broadcast

                // Reply with confiromation
                if (this.server.isRunning())
                {
                    Message reply = new AcknowledgeMessage(localNode.getNode());
                    try {
                        this.server.reply(origin, reply, commId);
                        me.log("Odeslan confirmation");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }



            }else {
            // i am not owner
                me.log("I am not owner");

            }

        };
        Thread thread = new Thread(task);
        thread.start();


    }

    @Override
    public void timeout(int conversationId) throws IOException {
        me.log("StoreMsgReciever message timeout.");
    }
}
