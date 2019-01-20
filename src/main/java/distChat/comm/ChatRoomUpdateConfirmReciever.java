package distChat.comm;

import distChat.model.ChatUser;
import distChat.model.ChatroomUpdateContent;
import kademlia.KadConfiguration;
import kademlia.KadServer;
import kademlia.KademliaNode;
import kademlia.dht.KademliaDHT;
import kademlia.message.Message;
import kademlia.message.Receiver;

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
            me.log("lookuping chatroom and sending msg again");

            Runnable task = () -> {
                var me = localNode.getChatUser();
                var localChatRoom = me.getInvolvedChatroomByName("Football");
                var chatRoomOwner = me.getStoredContactByKademliaId(localChatRoom.getOwnerId());
                me.sendChatRoomMessage(msg.getChatRoomMessage(), msg.getChatRoomName(), chatRoomOwner, false);
            };
            Thread thread = new Thread(task);
            thread.start();
        }


    }
}
