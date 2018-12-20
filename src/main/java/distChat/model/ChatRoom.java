package distChat.model;

import com.google.gson.Gson;
import distChat.Utils;
import kademlia.dht.KadContent;
import kademlia.node.KademliaId;
import kademlia.simulations.DHTContentImpl;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatRoom implements KadContent, Serializable {

    public static final transient String TYPE = "chatroom";

    private KademliaId key;

    private String name;

    private List<Message> messages = new ArrayList<>();

    private List<ChatRoomParticipant> participants = new ArrayList<>();

    private Date created;

    private Date lastUpdated;

    private String ownerId;


    public ChatRoom(String name, String ownerId) {
        this.key = new KademliaId(DigestUtils.sha1(name));
        this.name = name;
        this.created = new Date();
        this.lastUpdated = new Date();
        this.ownerId = ownerId.toString();
    }

    public List<ChatRoomParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ChatRoomParticipant> participants) {
        this.participants = participants;
    }

    public void setKey(KademliaId key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public KademliaId getKey() {
        return key;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public long getCreatedTimestamp() {
        return created.getTime();
    }

    @Override
    public long getLastUpdatedTimestamp() {
        return lastUpdated.getTime();
    }

    @Override
    public String getOwnerId() {
        return ownerId;
    }

    @Override
    public byte[] toSerializedForm() {
        Gson gson = new Gson();
        return gson.toJson(this).getBytes();
    }

    @Override
    public KadContent fromSerializedForm(byte[] data) {
        Gson gson = new Gson();
        return gson.fromJson(new String(data), DHTContentImpl.class);
    }

    @Override
    public String toString() {
        return "Room = name[" + name + "],ownerId[" + ownerId + "],msgs[" + messages.size() + "], updated[" + Utils.formatDate(lastUpdated, "dd.MM.yyyy HH:mm:ss") + "]";
    }
}
