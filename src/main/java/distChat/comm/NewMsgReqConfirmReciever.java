package distChat.comm;

import distChat.model.ChatRoom;
import distChat.model.ChatRoomMessage;
import distChat.model.ChatUser;
import kademlia.KadConfiguration;
import kademlia.KadServer;
import kademlia.KademliaNode;
import kademlia.dht.KademliaDHT;
import kademlia.message.Message;
import kademlia.message.Receiver;

import java.io.IOException;

public class NewMsgReqConfirmReciever implements Receiver {

    private final KadServer server;
    private final KademliaNode localNode;
    private final KademliaDHT dht;
    private final KadConfiguration config;
    private ChatUser me;

    private ChatRoomMessage msg;
    private Boolean firstAttempt;


    public NewMsgReqConfirmReciever(KadServer server, KademliaNode localNode, KademliaDHT dht, KadConfiguration config,
                                    ChatRoomMessage msg,
                                    Boolean firstAttempt) {
        this.server = server;
        this.localNode = localNode;
        this.dht = dht;
        this.config = config;
        this.msg = msg;
        this.firstAttempt= firstAttempt;
        this.me = localNode.getChatUser();
    }

    @Override
    public void receive(Message incoming, int commId) {
        me.log("NewMsgReqConfirmReciever - confirmed");
    }

    @Override
    public void timeout(int conversationId) throws IOException {
        me.log("NewMsgReqConfirmReciever - timeout");
        me.log("owner didnt respond");



        if(firstAttempt){
            me.log("lookuping chatroom and sending msg again");

            Runnable task = () -> {
                var me = localNode.getChatUser();
                var localChatRoom = me.getInvolvedChatroomByName("Football");
                var chatRoomOwner = me.getContactByKademliaId(localChatRoom.getOwnerId());
                me.sendMessage(msg,chatRoomOwner,false);
            };
            Thread thread = new Thread(task);
            thread.start();
        }



    }
}
