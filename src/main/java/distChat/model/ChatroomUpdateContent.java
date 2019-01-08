package distChat.model;

import com.google.gson.Gson;

public class ChatroomUpdateContent {

    private String chatRoomName;

    private ChatRoomMessage chatRoomMessage;

    private ChatRoomParticipant chatRoomParticipant;

    public ChatroomUpdateContent(String chatRoomName, ChatRoomMessage chatRoomMessage, ChatRoomParticipant chatRoomParticipant) {
        this.chatRoomName = chatRoomName;
        this.chatRoomMessage = chatRoomMessage;
        this.chatRoomParticipant = chatRoomParticipant;
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


    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }


    public static ChatroomUpdateContent fromJson(String data) {
        Gson gson = new Gson();
        return gson.fromJson(data, ChatroomUpdateContent.class);
    }


}
