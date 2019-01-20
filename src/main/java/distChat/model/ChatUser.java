package distChat.model;

import distChat.MessageLog;
import distChat.comm.ChatRoomUpdateConfirmReciever;
import distChat.comm.ChatRoomUpdateMessage;
import distChat.comm.JoinPrivateChatMessage;
import distChat.operation.OwnerBackupSchedulerOperation;
import distChat.operation.OwnerChatroomUpdateOperation;
import distChat.operation.StoragerCheckerOperation;
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
import java.util.*;

public class ChatUser {

    private OperationScheduler op;

    private MessageLog messageLog;

    private JKademliaNode kadNode;

    private String nickName;

    private HashMap<String, ChatRoom> chatRoomsInvolved = new HashMap<>();

    private HashMap<String, ChatRoom> chatRoomsOwned = new HashMap<>();

    private int uiPort;

    public ChatUser(String nickName, JKademliaNode kadNode) {
        this.kadNode = kadNode;
        this.nickName = nickName;
        this.messageLog = new MessageLog(nickName);
        this.op = new OperationScheduler(
                Set.of(
                        new StoragerCheckerOperation(this)
                        ,new OwnerBackupSchedulerOperation(this)
        ));

        this.log("UserNode ["+this.nickName+"] created, KademliaId ["+this.kadNode.getNode().getNodeId()+"]");
    }


    public void setUiPort(int uiPort) {
        this.uiPort = uiPort;
    }

    public int getUiPort() {
        return uiPort;
    }

    public String getNetworkStatus() {

        boolean running = this.getKadNode().getServer().isRunning();

        StringBuilder sb = new StringBuilder();

        sb.append("[");
        if (running) sb.append("RUNNING");
        if (!running) sb.append("OFFLINE");
        sb.append("]");
        sb.append(", Adress: [" + this.kadNode.getNode().getSocketAddress().getAddress().getHostAddress() + "]");
        sb.append(", Port: [" + this.kadNode.getPort() + "]");
        sb.append(", GUI: [" + this.uiPort + "]");

        return sb.toString();
    }

    public List<KademliaStorageEntryMetadata> getDHTStoredContent() {
        return this.getKadNode().getDHT().getStorageEntries();
    }

    public OperationScheduler getOp() {
        return op;
    }

    public Boolean isNodeRunning() {
        return this.getKadNode().getServer().isRunning();
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


    public void reconnect(JKademliaNode myNewNode) {
        this.chatRoomsInvolved = new HashMap<>();
        this.chatRoomsOwned = new HashMap<>();
        this.messageLog = new MessageLog(this.nickName);
        this.setKadNode(myNewNode);

        this.op = new OperationScheduler(
                Set.of(
                        new StoragerCheckerOperation(this)
                        ,new OwnerBackupSchedulerOperation(this)
                ));
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

    public HashMap<String, ChatRoom> getChatRoomsOwned() {
        return chatRoomsOwned;
    }

    public void storeChatroom(ChatRoom chatRoom, Boolean joinAlso) {

        if (joinAlso)
            chatRoom.getParticipants().add(new ChatRoomParticipant(this));

        try {
            this.kadNode.put(chatRoom);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (joinAlso)
            chatRoomsInvolved.put(chatRoom.getName(), chatRoom);

        this.chatRoomsOwned.put(chatRoom.getName(), chatRoom);
    }

    public void updateLocalCopy(ChatRoom chatroom) {
        chatRoomsInvolved.put(chatroom.getName(), chatroom);
    }

    public List<ChatRoom> getStoredChatrooms() {

        var returnValue = new ArrayList<ChatRoom>();

        for (KademliaStorageEntryMetadata storageEntry : this.getKadNode().getDHT().getStorageEntries()) {

            if (storageEntry.getType().equals(ChatRoom.TYPE)) {
                try {
                    returnValue.add(new ChatRoom().fromSerializedForm(this.getKadNode().getDHT().get(storageEntry).getContent()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return returnValue;
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

    public void sendChatRoomMessage(ChatRoomMessage message, String chatRoomName, Contact targetContact, Boolean firstAttempt) {

        messageLog.log("Sending new message to chatroom" + chatRoomName + " to contact " + targetContact.getNode().getNodeId());

        if (targetContact.getNode().getNodeId().equals(this.kadNode.getNode().getNodeId())) {
            messageLog.log("Owner sending message to his own room!");

            this.op.queueOperation(
                    new OwnerChatroomUpdateOperation(
                            this,
                            chatRoomName,
                            new ChatRoomUpdateMessage(chatRoomName, message, this.getKadNode().getNode()),
                            0));


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


        if (getInvolvedChatroomByName(chatRoomName) != null) {
            if (getInvolvedChatroomByName(chatRoomName).getParticipants().contains(new ChatRoomParticipant(this))) {
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

    public void shutdown() {
        this.getKadNode().getServer().shutdown();
        this.getOp().setShutdown(true);
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

    public Contact getStoredContactByKademliaId(String kademliaIdString) {

        KademliaId kademliaId = new KademliaId((DigestUtils.sha1(kademliaIdString)));

        return getContacts().stream()
                .filter(contact -> kademliaId.equals(contact.getNode().getNodeId()))
                .findAny()
                .orElse(null);
    }

    public void inviteToPrivateChat(Contact targetContact, String chatroomname){

        try {
            getKadNode().getServer().sendMessage(
                    targetContact.getNode(),
                    new JoinPrivateChatMessage(chatroomname,this.kadNode.getNode())
                    ,null);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
