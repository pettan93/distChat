package distChat.comm;

import distChat.model.ChatUser;
import kademlia.KademliaNode;
import kademlia.message.Message;
import kademlia.message.Receiver;

import java.io.IOException;

public class ChatRoomUpdateBroadcastReciever implements Receiver {

    private final KademliaNode localNode;
    private ChatUser me;

    public ChatRoomUpdateBroadcastReciever(KademliaNode localNode) {
        this.localNode = localNode;
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

        if (msg.getContent().getNewOwner() != null) {
            myChatroomCopy.setOwnerId(msg.getContent().getNewOwner());
        }


        me.updateInvolvedChatroomByName(myChatroomCopy.getName(), myChatroomCopy);

    }

    @Override
    public void timeout(int conversationId) throws IOException {
        me.log("MesBroadcastUpdateReciever message timeout.");
    }
}
