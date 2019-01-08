package distChat.model;

import distChat.MessageLog;
import distChat.comm.ChatRoomUpdateConfirmReciever;
import distChat.comm.ChatRoomUpdateMessage;
import kademlia.JKademliaNode;
import kademlia.dht.GetParameter;
import kademlia.dht.JKademliaStorageEntry;
import kademlia.dht.KademliaStorageEntry;
import kademlia.dht.KademliaStorageEntryMetadata;
import kademlia.exceptions.ContentNotFoundException;
import kademlia.node.KademliaId;
import kademlia.routing.Contact;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatUser {

    public MessageLog messageLog;

    private JKademliaNode kadNode;

    private String nickName;

    private HashMap<String, ChatRoom> chatRoomsInvolved = new HashMap<>();

    public ChatUser(String nickName, JKademliaNode kadNode) {
        this.kadNode = kadNode;
        this.nickName = nickName;
        this.messageLog = new MessageLog(nickName);
    }

    public MessageLog getMessageLog() {
        return messageLog;
    }

    public void setMessageLog(MessageLog messageLog) {
        this.messageLog = messageLog;
    }

    public void log(String content) {
        this.messageLog.log(content);
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

    public HashMap<String, ChatRoom> getChatRoomsInvolved() {
        return chatRoomsInvolved;
    }

    public void storeChatroom(ChatRoom chatRoom) {
        try {
            this.kadNode.put(chatRoom);
        } catch (IOException e) {
            e.printStackTrace();
        }
        chatRoomsInvolved.put(chatRoom.getName(), chatRoom);
    }

    public ChatRoom getStoredChatroom(String chatroomName) {

        KademliaId chatroomKey = new KademliaId(DigestUtils.sha1(chatroomName));

        KademliaStorageEntryMetadata chatromMetaData = null;
        KademliaStorageEntry chatroomEntry = null;
        ChatRoom localChatRoom = null;
        for (KademliaStorageEntryMetadata storageEntry : this.getKadNode().getDHT().getStorageEntries()) {
            if (storageEntry.getKey().equals(chatroomKey)) {
                chatromMetaData = storageEntry;
            }
        }


        if (chatromMetaData != null) {
            try {
                chatroomEntry = this.getKadNode().getDHT().retrieve(chatroomKey, chatromMetaData.hashCode());
                localChatRoom = new ChatRoom().fromSerializedForm(chatroomEntry.getContent());

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return localChatRoom;
    }


    public Contact lookupUserByName(String userName) {
        return null;
    }


    public ChatRoom lookupChatRoomByName(String chatRoomName) {

        KademliaId chatRoomId = new KademliaId(DigestUtils.sha1(chatRoomName));
        GetParameter gp = new GetParameter(chatRoomId, ChatRoom.TYPE);

        ChatRoom foundedChatRoom = null;

        try {
            JKademliaStorageEntry found = this.getKadNode().get(gp);

            foundedChatRoom = (ChatRoom) new ChatRoom().fromSerializedForm(found.getContent());

        } catch (ContentNotFoundException | IOException e) {
            e.printStackTrace();
        }

        return foundedChatRoom;
    }

    public void sendMessage(ChatRoomMessage message, String chatRoomName, Contact targetContact, Boolean firstAttempt) {

        messageLog.log("Sending new message to chatroom" + chatRoomName);

        if (targetContact.getNode().getNodeId().equals(this.kadNode.getNode().getNodeId())) {
            messageLog.log("Owner sending message to his own room!");

            // TODO !

        } else {
            try {
                getKadNode().getServer().sendMessage(
                        targetContact.getNode(),
                        new ChatRoomUpdateMessage(chatRoomName, message, this.getKadNode().getNode()),
                        new ChatRoomUpdateConfirmReciever(
                                this.getKadNode().getServer(),
                                this.kadNode,
                                this.kadNode.getDHT(),
                                this.kadNode.getCurrentConfiguration(),
                                new ChatroomUpdateContent(chatRoomName, message, null),
                                firstAttempt));


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void joinChatroom(ChatRoomParticipant participant, String chatRoomName, Contact targetContact) {

        messageLog.log("joining to chatroom" + chatRoomName);


        if(getInvolvedChatroomByName(chatRoomName) != null){
            if(getInvolvedChatroomByName(chatRoomName).getParticipants().contains(new ChatRoomParticipant(this))){
                messageLog.log("Cant join to chat, where you already are");
                return;
            }
        }

        try {
            getKadNode().getServer().sendMessage(
                    targetContact.getNode(),
                    new ChatRoomUpdateMessage(chatRoomName, participant, this.getKadNode().getNode()),
                    new ChatRoomUpdateConfirmReciever(
                            this.getKadNode().getServer(),
                            this.kadNode,
                            this.kadNode.getDHT(),
                            this.kadNode.getCurrentConfiguration(),
                            new ChatroomUpdateContent(chatRoomName, null, participant),
                            true));


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String toString() {
        return "ChatUser{" +
                ", nickName='" + nickName + '\'' +
                ", chatRoomsInvolved=" + chatRoomsInvolved +
                "kadNode=" + kadNode +
                '}';
    }


    public ChatRoom getInvolvedChatroomByName(String chatRoomName) {

        return chatRoomsInvolved.get(chatRoomName);

    }

    public void updateInvolvedChatroomByName(String chatRoomName, ChatRoom chatRoom) {
        this.chatRoomsInvolved.put(chatRoomName, chatRoom);
    }

    public List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<>();
        for (Object o : getKadNode().getRoutingTable().getAllContacts()) {
            if (o instanceof Contact) {
                contacts.add((Contact) o);
            }
        }
        return contacts;
    }

    public Contact getContactByKademliaId(String kademliaIdString) {

        KademliaId kademliaId = new KademliaId((DigestUtils.sha1(kademliaIdString)));

        return getContacts().stream()
                .filter(contact -> kademliaId.equals(contact.getNode().getNodeId()))
                .findAny()
                .orElse(null);
    }
}
