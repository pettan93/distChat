package distChat.model;

import com.google.gson.Gson;

import java.util.Objects;

public class ChatRoomParticipant {

    private String nickName;

    public ChatRoomParticipant(ChatUser chatUser) {
        this.nickName = chatUser.getNickName();
    }

    public ChatRoomParticipant(String nickName) {
        this.nickName = nickName;
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
                "nickName='" + nickName + '\'' +
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatRoomParticipant that = (ChatRoomParticipant) o;
        return Objects.equals(nickName, that.nickName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickName);
    }
}
