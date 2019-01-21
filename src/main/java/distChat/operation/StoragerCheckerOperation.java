package distChat.operation;

import distChat.comm.AreYouAwakeConfirmReciever;
import distChat.comm.AreYouAwakeMessage;
import distChat.comm.ChatRoomUpdateMessage;
import distChat.model.ChatRoom;
import distChat.model.ChatUser;
import kademlia.exceptions.ContentNotFoundException;
import kademlia.exceptions.RoutingException;
import kademlia.node.Node;
import kademlia.operation.Operation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// action which perform storagers periodically to check if owner is alive
// if not, one of storagers becomes owner
public class StoragerCheckerOperation implements Operation, RepeatOperation {

    private ChatUser me;


    public StoragerCheckerOperation(ChatUser me) {
        this.me = me;
    }

    @Override
    public void execute() throws IOException, RoutingException {
//        me.log("StoragerCheckerOperation --->");

        // if I am dead, do nothing
        if (!me.getKadNode().getServer().isRunning()) {
            return;
        }

        var chatrooms = me.getStoredChatrooms();


        if (!chatrooms.isEmpty()) {

//            deleteOldCopiesOfChatrooms();

            for (ChatRoom chatroom : chatrooms) {

                // if i am owner, then no need to check if i am alive
                if (chatroom.getOwnerId().equals(me.getNickName())) {
                    return;
                }

//                me.log("StoragerCheckerOperation for chatroom "+chatroom.getName());

                // is chatroom owner offline?
                var ownerId = chatroom.getOwnerId();
                var ownerNode = me.getStoredContactByKademliaId(chatroom.getOwnerId());
                var areYouAwakeConfirmReciever = new AreYouAwakeConfirmReciever();
                me.getKadNode().getServer().sendMessage(
                        ownerNode.getNode(),
                        new AreYouAwakeMessage(me.getKadNode().getNode()),
                        areYouAwakeConfirmReciever);
                var ownerAlive = areYouAwakeConfirmReciever.getResult();

                //
                if (!ownerAlive) { // owner not alive!
                    me.log(" " + ownerId + " of chatroom " + chatroom.getName() + " not alive");

                    // asks all storagers if they are alive
                    // create array of dead storagers
                    var deadStoragers = new ArrayList<Node>();


                    me.log("try contact storagers");


                    // let contact every other storages (not me) if is alive
                    for (Node n : chatroom.getStoragers()) {

                        // if iterating me, i m alive
                        if (n.getNodeId().equals(me.getKadNode().getNode().getNodeId())) {
                            continue;
                        }

                        var storagerAwakeReciever = new AreYouAwakeConfirmReciever();
                        me.getKadNode().getServer().sendMessage(
                                n,
                                new AreYouAwakeMessage(
                                        me.getKadNode().getNode()), storagerAwakeReciever);
                        var storagerAwake = storagerAwakeReciever.getResult();
                        if (!storagerAwake) {
                            me.log("storager[" + n.getNodeId() + "] is DEAD");
                            deadStoragers.add(n);
                        }
                    }


                    me.log("DeadStoragers = " + deadStoragers);

                    // let choose new owner from storagers which are alive
                    var amINextOwner = amINextOwner(chatroom, deadStoragers);

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
                } else {
                    // owner alive, no need to do anything
                }

            }


        }

//        me.log("StoragerCheckerOperation <----");
    }

    private Boolean amINextOwner(ChatRoom chatroom, List<Node> deadStoragers) {

        Node myNode = me.getKadNode().getNode();

        Node newOwner = null;

        boolean newOwnerChoosen = false;

        for (int i = 0; i < chatroom.getStoragers().size(); i++) {
            if (newOwnerChoosen) break;
            if (!deadStoragers.contains(chatroom.getStoragers().get(i))) {
                newOwner = chatroom.getStoragers().get(i);
                newOwnerChoosen = true;
            }
        }

        // clean deadNodes
        if(newOwner == null){
            me.log("ERROR, new owner wasnt choosed");
            return false;
        }

        return newOwner.equals(myNode);
    }


    @Override
    public int getInterval() {
        return 2000;
    }
}
