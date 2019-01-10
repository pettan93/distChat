package distChat.operation;

import distChat.comm.ChatRoomUpdateMessage;
import distChat.model.ChatRoom;
import distChat.model.ChatRoomParticipant;
import distChat.model.ChatUser;
import kademlia.KadServer;
import kademlia.exceptions.ContentNotFoundException;
import kademlia.exceptions.RoutingException;
import kademlia.operation.Operation;

import java.io.IOException;

public class OwnerChatroomUpdateOperation implements Operation, BlockingOperation {

    private ChatUser me;
    private String targetChatRoomName;
    private ChatRoomUpdateMessage msg;

    private int commId;

    public OwnerChatroomUpdateOperation(ChatUser me, String targetChatRoomName, ChatRoomUpdateMessage msg, int commId) {
        this.me = me;
        this.targetChatRoomName = targetChatRoomName;
        this.msg = msg;
        this.commId = commId;
    }

    @Override
    public void execute() throws IOException, RoutingException {

        block();

        me.log("Comm[" + commId + "] i am chatowner, gonna store updated chatroom");

        // get chatroom from storagers
        var targetChatRoom = me.lookupChatRoomByName(msg.getContent().getChatRoomName());

//         delete local copy
        try {
            me.getKadNode().getDHT().remove(targetChatRoom);
        } catch (ContentNotFoundException e) {
            e.printStackTrace();
        }

        if (msg.getContent().getChatRoomMessage() != null) {
            targetChatRoom.addMessage(msg.getContent().getChatRoomMessage());
        }

        if (msg.getContent().getChatRoomParticipant() != null) {
            targetChatRoom.addParticipants(msg.getContent().getChatRoomParticipant());
        }

        if (msg.getContent().getNewOwner() != null) {
            targetChatRoom.setOwnerId(msg.getContent().getNewOwner());
        }


        me.storeChatroom(targetChatRoom,false);


        System.out.println(targetChatRoom.getParticipants());
        if(targetChatRoom.getParticipants().contains(new ChatRoomParticipant(me))){
//            me.log("owner si update svoji copy");
            me.updateLocalCopy(targetChatRoom);
        }else {
//            me.log(":(");
        }

        me.log("storing to DHT " + targetChatRoom);

        // broadcast

        me.log("Comm[" + commId + "] broadcast time!");

        var broadcasMsg = new ChatRoomUpdateMessage(msg, me.getKadNode().getNode());
        for (ChatRoomParticipant participant : targetChatRoom.getParticipants()) {

            // dont sent broadcast to me
            if (participant.getNickName().equals(me.getNickName())) {
                me.log("Comm[" + commId + "] skipping [" + participant.getNickName() + "] bcse owner");
                continue;
            }

            var broadcastTarget = me.getContactByKademliaId(participant.getNickName());

            me.log("Comm[" + commId + "] - sending broadcast to participant " + participant.getNickName());

            if (broadcastTarget == null) {
                me.log("Comm[" + commId + "] cant send broacast beceause dont have contact");
            } else {
                try {
                    me.getKadNode().getServer().sendMessage(broadcastTarget.getNode(), broadcasMsg, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        release();
    }


    @Override
    public void block() {
        me.getOp().changeOperationLock(true);
    }

    @Override
    public void release() {
        me.getOp().changeOperationLock(false);
    }
}
