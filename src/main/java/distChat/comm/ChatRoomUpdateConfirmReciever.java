package distChat.comm;

import distChat.MyUtils;
import distChat.model.ChatRoomParticipant;
import distChat.model.ChatUser;
import distChat.model.ChatroomUpdateContent;
import kademlia.KadConfiguration;
import kademlia.KadServer;
import kademlia.KademliaNode;
import kademlia.dht.KademliaDHT;
import kademlia.message.Message;
import kademlia.message.Receiver;
import kademlia.operation.NodeLookupOperation;

import java.io.IOException;

public class ChatRoomUpdateConfirmReciever implements Receiver {

    private final KadServer server;
    private final KademliaNode localNode;
    private final KademliaDHT dht;
    private final KadConfiguration config;
    private ChatUser me;

    private ChatroomUpdateContent msg;
    private Boolean firstAttempt;


    public ChatRoomUpdateConfirmReciever(KadServer server, KademliaNode localNode, KademliaDHT dht, KadConfiguration config,
                                         ChatroomUpdateContent msg,
                                         Boolean firstAttempt) {
        this.server = server;
        this.localNode = localNode;
        this.dht = dht;
        this.config = config;
        this.msg = msg;
        this.firstAttempt = firstAttempt;
        this.me = localNode.getChatUser();
    }

    @Override
    public void receive(Message incoming, int commId) {
        me.log("MsgReqConfirmReciever - confirmed");
    }

    @Override
    public void timeout(int conversationId) throws IOException {
        me.log("NewMsgReqConfirmReciever - timeout");
        me.log("owner didnt respond");


        if (firstAttempt) {
            me.log("lookuping chatroom (because of new owner) and sending msg again");

            Runnable task = () -> {
                var me = localNode.getChatUser();
                var localChatRoom = me.getInvolvedChatroomByName("Football");
                var chatRoomOwnerId = localChatRoom.getOwnerId();
                var ownerContact = me.getStoredContactByKademliaId(chatRoomOwnerId);

                if (ownerContact != null) {
                    me.log("i have new owner contact ! sending msg to him");
                    me.sendChatRoomMessage(msg.getChatRoomMessage(), msg.getChatRoomName(), ownerContact, false);

                } else {
                    me.log("I dont have contact for new chatroom owner! lookuping..");
                    NodeLookupOperation ndlo = new NodeLookupOperation(
                            me.getKadNode().getServer(),
                            me.getKadNode(),
                            MyUtils.kademliaId(chatRoomOwnerId),
                            me.getKadNode().getCurrentConfiguration());
                    try {
                        ndlo.execute();
                        ownerContact = me.getStoredContactByKademliaId(chatRoomOwnerId);

                        if (ownerContact != null) {
                            me.log("i lookuped new owner ! sending msg to him");
                            me.sendChatRoomMessage(msg.getChatRoomMessage(), msg.getChatRoomName(), ownerContact, false);
                        } else {
                            me.log("MESSAGE LOST");
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            };
            Thread thread = new Thread(task);
            thread.start();
        }


    }
}
