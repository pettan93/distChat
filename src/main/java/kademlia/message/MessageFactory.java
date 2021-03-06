package kademlia.message;


import distChat.comm.*;
import kademlia.KadConfiguration;
import kademlia.KadServer;
import kademlia.KademliaNode;
import kademlia.dht.KademliaDHT;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Handles creating messages and receivers
 *
 * @author Joshua Kissoon
 * @since 20140202
 */
public class MessageFactory implements KademliaMessageFactory {

    private final KademliaNode localNode;
    private final KademliaDHT dht;
    private final KadConfiguration config;

    public MessageFactory(KademliaNode local, KademliaDHT dht, KadConfiguration config) {
        this.localNode = local;
        this.dht = dht;
        this.config = config;
    }

    @Override
    public Message createMessage(byte code, DataInputStream in) throws IOException {

//        System.out.println("create message " + code);

        switch (code) {
            case AcknowledgeMessage.CODE:
                return new AcknowledgeMessage(in);
            case ConnectMessage.CODE:
                return new ConnectMessage(in);
            case ContentMessage.CODE:
                return new ContentMessage(in);
            case ContentLookupMessage.CODE:
                return new ContentLookupMessage(in);
            case NodeLookupMessage.CODE:
                return new NodeLookupMessage(in);
            case NodeReplyMessage.CODE:
                return new NodeReplyMessage(in);
            case SimpleMessage.CODE:
                return new SimpleMessage(in);
            case StoreContentMessage.CODE:
                return new StoreContentMessage(in);
            case ChatRoomUpdateMessage.CODE: // TODO PETTAN
                return new ChatRoomUpdateMessage(in);
            case WhoAreYouMessage.CODE: // TODO PETTAN
                return new WhoAreYouMessage(in);
            case AreYouAwakeMessage.CODE: // TODO PETTAN
                return new AreYouAwakeMessage(in);
            case JoinPrivateChatMessage.CODE: // TODO PETTAN
                return new JoinPrivateChatMessage(in);
            case MyNameIsMessage.CODE: // TODO PETTAN
                return new MyNameIsMessage(in);
            default: {
                System.out.println(this.localNode.getChatUser().getNickName() + " - No Message handler found for message. Code: " + code);
                return new SimpleMessage(in);
            }


        }
    }

    @Override
    public Receiver createReceiver(byte code, KadServer server) {
        switch (code) {
            case ConnectMessage.CODE:
                return new ConnectReceiver(server, this.localNode);
            case ContentLookupMessage.CODE:
                return new ContentLookupReceiver(server, this.localNode, this.dht, this.config);
            case NodeLookupMessage.CODE:
                return new NodeLookupReceiver(server, this.localNode, this.config);
            case StoreContentMessage.CODE:
                return new StoreContentReceiver(server, this.localNode, this.dht);
            case ChatRoomUpdateMessage.CODE: // TODO PETTAN
                return new ChatRoomUpdateReciever(server, this.localNode, this.dht, this.config);
            case AreYouAwakeMessage.CODE: // TODO PETTAN
                return new AreYouAwakeReciever(this.localNode);
            case WhoAreYouMessage.CODE:  // TODO PETTAN
                return new WhoAreYouReciever(this.localNode);
            case MyNameIsMessage.CODE:  // TODO PETTAN
                return new MyNameIsReciever(this.localNode);
            case JoinPrivateChatMessage.CODE:  // TODO PETTAN
                return new JoinPrivateChatReciever(this.localNode);

            default:
                System.out.println("No receiver found for message. Code: " + code);
                return new SimpleReceiver();
        }
    }
}
