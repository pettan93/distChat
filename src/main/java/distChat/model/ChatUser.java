package distChat.model;

import kademlia.JKademliaNode;
import kademlia.routing.Contact;

import java.io.IOException;

public class ChatUser implements IChatUser {

    private JKademliaNode kadNode;

    private String nickName;

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


    public void bootstrap(ChatUser chatNode){
        try {
            this.getKadNode().bootstrap(chatNode.getKadNode().getNode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void storeChatroom(ChatRoom chatRoom){
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
        return null;
    }
}
