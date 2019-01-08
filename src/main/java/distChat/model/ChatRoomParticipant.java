package distChat.model;

import com.google.gson.Gson;

import java.util.Objects;

public class ChatRoomParticipant {

    private String chatroomName;

    private String nickName;

    public ChatRoomParticipant(ChatUser chatUser) {
        this.nickName = chatUser.getNickName();
    }

    public ChatRoomParticipant(String kademliaId, String nickName) {
        this.nickName = nickName;
    }

    public String getChatroomName() {
        return chatroomName;
    }

    public void setChatroomName(String chatroomName) {
        this.chatroomName = chatroomName;
    }


    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


    @Override
    public String toString() {
        return "ChatRoomParticipant{" +
                "chatroomName='" + chatroomName + '\'' +
                '}';
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }


    public static ChatRoomParticipant fromJson(String data) {
        Gson gson = new Gson();
        return gson.fromJson(data, ChatRoomParticipant.class);
    }
}
