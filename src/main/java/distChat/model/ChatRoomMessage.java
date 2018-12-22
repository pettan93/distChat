package distChat.model;

import com.google.gson.Gson;
import kademlia.dht.KadContent;
import kademlia.simulations.DHTContentImpl;

import java.io.Serializable;
import java.util.Date;

public class ChatRoomMessage implements Serializable {

    private String chatRoomId;

    private String senderName;

    private String content;

    private Date sendTime;

    public ChatRoomMessage(String senderName, String content) {
        this.senderName = senderName;
        this.content = content;
        this.sendTime = new Date();
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }


    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }


    public static ChatRoomMessage fromJson(String data) {
        Gson gson = new Gson();
        return gson.fromJson(data, ChatRoomMessage.class);
    }

    @Override
    public String toString() {
        return "ChatRoomMessage{" +
                "senderName='" + senderName + '\'' +
                ", content='" + content + '\'' +
                ", sendTime=" + sendTime +
                '}';
    }
}
