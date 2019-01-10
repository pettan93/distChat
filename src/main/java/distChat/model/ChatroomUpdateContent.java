package distChat.model;

import com.google.gson.Gson;

public class ChatroomUpdateContent {

    private String chatRoomName;

    private ChatRoomMessage chatRoomMessage;

    private ChatRoomParticipant chatRoomParticipant;

    private String newOwner;

    public ChatroomUpdateContent(String chatRoomName, ChatRoomMessage chatRoomMessage, ChatRoomParticipant chatRoomParticipant) {
        this.chatRoomName = chatRoomName;
        this.chatRoomMessage = chatRoomMessage;
        this.chatRoomParticipant = chatRoomParticipant;
    }

    public ChatroomUpdateContent(String chatRoomName, String newOwner) {
        this.chatRoomName = chatRoomName;
        this.newOwner = newOwner;
    }

    public String getChatRoomName() {
        return chatRoomName;
    }

    public ChatRoomMessage getChatRoomMessage() {
        return chatRoomMessage;
    }

    public ChatRoomParticipant getChatRoomParticipant() {
        return chatRoomParticipant;
    }

    public String getNewOwner() {
        return newOwner;
    }

    public void setNewOwner(String newOwner) {
        this.newOwner = newOwner;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }


    public static ChatroomUpdateContent fromJson(String data) {
        Gson gson = new Gson();
        return gson.fromJson(data, ChatroomUpdateContent.class);
    }

    @Override
    public String toString() {
        return "ChatroomUpdateContent{" +
                "chatRoomName='" + chatRoomName + '\'' +
                ", chatRoomMessage=" + chatRoomMessage +
                ", chatRoomParticipant=" + chatRoomParticipant +
                ", newowner=" + newOwner +
                '}';
    }
}
