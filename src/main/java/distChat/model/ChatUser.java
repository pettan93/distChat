package distChat.model;

import distChat.comm.StoreMsgReqMessage;
import kademlia.JKademliaNode;
import kademlia.dht.GetParameter;
import kademlia.dht.JKademliaStorageEntry;
import kademlia.exceptions.ContentNotFoundException;
import kademlia.node.KademliaId;
import kademlia.node.Node;
import kademlia.routing.Contact;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatUser implements IChatUser {

    private JKademliaNode kadNode;

    private String nickName;

    private List<ChatRoom> chatRoomsInvolved = new ArrayList<>();

    public ChatUser(String nickName, JKademliaNode kadNode) {
        this.kadNode = kadNode;
        this.nickName = nickName;
    }

    public JKademliaNode getKadNode() {
        return kadNode;
    }

    public void setKadNode(JKademliaNode kadNode) {
        this.kadNode = kadNode;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


    public void bootstrap(ChatUser chatNode) {
        try {
            this.getKadNode().bootstrap(chatNode.getKadNode().getNode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void storeChatroom(ChatRoom chatRoom) {
        try {
            this.kadNode.put(chatRoom);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Contact lookupUserByName(String userName) {
        return null;
    }

    @Override
    public ChatRoom lookupChatRoomByName(String chatRoomName) {

        KademliaId chatRoomId = new KademliaId(DigestUtils.sha1(chatRoomName));
        GetParameter gp = new GetParameter(chatRoomId, ChatRoom.TYPE);

        ChatRoom foundedChatRoom = null;

        try {
            JKademliaStorageEntry found = this.getKadNode().get(gp);

            foundedChatRoom = (ChatRoom) new ChatRoom().fromSerializedForm(found.getContent());

            chatRoomsInvolved.add(foundedChatRoom);

        } catch (ContentNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return foundedChatRoom;

    }

    @Override
    public void sendMessage(ChatRoomMessage message, ChatRoom chatRoom) {

        message.setChatRoomId(chatRoom.getKey().toString());

        // TODO lookup user by id
        Contact chatRoomOwner = null;

        for(Contact c : (List<Contact>) getKadNode().getRoutingTable().getAllContacts()){
            System.out.println(c.getNode());
            if(c.getNode().getNodeId().toString().equals(chatRoom.getOwnerId())){
                chatRoomOwner = c;
            }
        }

        System.out.println(chatRoomOwner);


        try {
            getKadNode().getServer().sendMessage(
                    chatRoomOwner.getNode(),
                    new StoreMsgReqMessage(new ChatRoomMessage("Anna", "ahoj")),
                    null);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
