package distChat.operation;

import distChat.comm.AreYouAwakeConfirmReciever;
import distChat.comm.AreYouAwakeMessage;
import distChat.comm.ChatRoomUpdateMessage;
import distChat.model.ChatRoom;
import distChat.model.ChatRoomParticipant;
import distChat.model.ChatUser;
import kademlia.KadServer;
import kademlia.dht.KademliaStorageEntry;
import kademlia.dht.KademliaStorageEntryMetadata;
import kademlia.exceptions.ContentNotFoundException;
import kademlia.exceptions.RoutingException;
import kademlia.node.Node;
import kademlia.operation.Operation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class StoragerCheckerOperation implements Operation, RepeatOperation {

    private ChatUser me;


    public StoragerCheckerOperation(ChatUser me) {
        this.me = me;
    }

    @Override
    public void execute() throws IOException, RoutingException {
//        me.log("StoragerCheckerOperation --->");


        var chatrooms = me.getStoredChatrooms();


        if (!chatrooms.isEmpty()) {

//            deleteOldCopiesOfChatrooms();

            for (ChatRoom chatroom : chatrooms) {

                if (chatroom.getOwnerId().equals(me.getNickName())) {
                    return;
                }

                // is chatroom owner offline?
                var ownerId = chatroom.getOwnerId();

                var ownerNode = me.getContactByKademliaId(chatroom.getOwnerId());


                var areYouAwakeConfirmReciever = new AreYouAwakeConfirmReciever();
                me.getKadNode().getServer().sendMessage(
                        ownerNode.getNode(),
                        new AreYouAwakeMessage(me.getKadNode().getNode()),
                        areYouAwakeConfirmReciever);
                var ownerAlive = areYouAwakeConfirmReciever.getResult();


                if (!ownerAlive) {
                    me.log(" " + ownerId + " of chatroom " +  chatroom.getName() +" not alive");
                    var amINextOwner = amINextOwner(chatroom);

                    if (amINextOwner) {
                        me.log(" I am next owner! ");
                        chatroom.setOwnerId(me.getNickName());

                        me.getOp().queueOperation(
                                new OwnerChatroomUpdateOperation(
                                        me,
                                        chatroom.getName(),
                                        new ChatRoomUpdateMessage(chatroom.getName(), me.getNickName(), me.getKadNode().getNode()),
                                        0));
                    } else {
                        me.log(" But I am not next owner ");
                        try {
                            me.getKadNode().getDHT().remove(chatroom);
                        } catch (ContentNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }


        }

//        me.log("StoragerCheckerOperation <----");
    }

    private Boolean amINextOwner(ChatRoom chatroom) {

        Node myNode = me.getKadNode().getNode();

        return chatroom.getStoragers().get(0).equals(myNode);
    }



    @Override
    public int getInterval() {
        return 2000;
    }
}
