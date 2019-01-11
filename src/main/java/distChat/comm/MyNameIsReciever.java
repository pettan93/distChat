package distChat.comm;

import distChat.model.ChatUser;
import distChat.model.ChatUserSearchResult;
import kademlia.KademliaNode;
import kademlia.message.Message;
import kademlia.message.Receiver;

import java.io.IOException;

public class MyNameIsReciever implements Receiver {

    private final KademliaNode localNode;
    private ChatUser me;

    private ChatUserSearchResult result;

    public MyNameIsReciever(KademliaNode localNode) {
        this.localNode = localNode;
        this.me = localNode.getChatUser();
    }

    @Override
    public void receive(Message incoming, int commId) {
        me.log("Comm[" + commId + "] I get 'My name is' message");

        var msg = (MyNameIsMessage) incoming;

        this.result = new ChatUserSearchResult(msg.getOrigin(),msg.getNickname());

    }

    public ChatUserSearchResult getResult() {
        return result;
    }

    @Override
    public void timeout(int conversationId) throws IOException {
        me.log("MyNameIsReciever message timeout.");
    }
}
