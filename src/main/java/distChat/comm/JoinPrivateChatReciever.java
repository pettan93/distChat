package distChat.comm;

import distChat.model.ChatRoomParticipant;
import distChat.model.ChatUser;
import distChat.model.ChatUserSearchResult;
import kademlia.KademliaNode;
import kademlia.message.Message;
import kademlia.message.Receiver;
import kademlia.routing.Contact;

import java.io.IOException;

public class JoinPrivateChatReciever implements Receiver {

    private final KademliaNode localNode;
    private ChatUser me;


    public JoinPrivateChatReciever(KademliaNode localNode) {
        this.localNode = localNode;
        this.me = localNode.getChatUser();
    }

    @Override
    public void receive(Message incoming, int commId) {
        me.log("Comm[" + commId + "] I get 'JoinPrivateChatReciever' message");

        var msg = (JoinPrivateChatMessage) incoming;

        me.joinChatroom(new ChatRoomParticipant(me),msg.getContent(),new Contact(msg.getOrigin()));

    }

    @Override
    public void timeout(int conversationId) throws IOException {
        me.log("JoinPrivateChatReciever timeout.");
    }
}
