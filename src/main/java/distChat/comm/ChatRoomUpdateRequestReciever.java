package distChat.comm;

import distChat.MyUtils;
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

public class ChatRoomUpdateRequestReciever implements Receiver {

    private final KadServer server;
    private final KademliaNode localNode;
    private final KademliaDHT dht;
    private final KadConfiguration config;
    private ChatUser me;

    public ChatRoomUpdateRequestReciever(KadServer server, KademliaNode localNode, KademliaDHT dht, KadConfiguration config) {
        this.server = server;
        this.localNode = localNode;
        this.dht = dht;
        this.config = config;
        this.me = localNode.getChatUser();
    }

    @Override
    public void receive(Message incoming, int commId) {
        me.log("Recieved storeMsgRequest: " + incoming);

        ChatRoomUpdateMessage msg = (ChatRoomUpdateMessage) incoming;
        Node origin = msg.getOrigin();


        var targetChatroomName = msg.getContent().getChatRoomName();

        Runnable task = () -> {

            ChatRoom targetChatRoom = me.lookupChatRoomByName(targetChatroomName);

            // if I am owner
            if (targetChatRoom.getOwnerId().equals(me.getNickName())) {

                me.log("jsem chatowner, storing chatroom with new message / ¨participant");


                if (msg.getContent().getChatRoomMessage() != null) {
                    targetChatRoom.addMessage(msg.getContent().getChatRoomMessage());
                }

                boolean newParticipant = false;
                if (msg.getContent().getChatRoomParticipant() != null) {
                    me.log("new participant!");
                    newParticipant = true;
                    targetChatRoom.addParticipants(msg.getContent().getChatRoomParticipant());
                }


                me.storeChatroom(targetChatRoom);


                // Reply with confiromation
                if (this.server.isRunning()) {
                    Message reply = new AcknowledgeMessage(localNode.getNode());
                    try {
                        this.server.reply(origin, reply, commId);
                        me.log("Odeslan confirmation");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                // broadcast

                me.log("broadcast time!");

                var broadcasMsg = new ChatRoomUpdateMessage(msg, me.getKadNode().getNode());
                for (ChatRoomParticipant participant : targetChatRoom.getParticipants()) {


                    if (participant.getNickName().equals(me.getNickName())){
                        me.log("skipping ["+participant.getNickName()+"] bcse owner");
                        continue;
                    }


//                    if (!newParticipant && MyUtils.nickNameToKademliaHex(participant.getNickName()).equals(origin.getNodeId().toString())){
//                        me.log("skipping ["+participant.getNickName()+"] he needs only confirm");
//                        continue;
//                    }



                    var broadcastTarget = me.getContactByKademliaId(participant.getNickName());

                    System.out.println(" - sending broadcast to participant " + participant.getNickName());

                    if (broadcastTarget == null) {
                        me.log("cant send broacast beceause dont have contact");
                    }else {
                        try {
                            this.server.sendMessage(broadcastTarget.getNode(), broadcasMsg, null);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }


            } else {
                // i am not owner
                me.log("I am not owner");

            }

        };
        Thread thread = new Thread(task);
        thread.start();


    }

    @Override
    public void timeout(int conversationId) throws IOException {
        me.log("MesReqUpdateReciever message timeout.");
    }
}
