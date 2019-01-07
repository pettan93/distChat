package distChat.UI;

import distChat.model.ChatRoom;
import distChat.model.ChatRoomMessage;
import distChat.model.ChatUser;

public class ActionProcessor {

    public static boolean processSendMessage(ChatUser chatUser, String chatRoomName, String message) {

        chatUser.log("Send msg process");

        var chatroom = chatUser.getInvolvedChatroomByName(chatRoomName);

        var chatRoomMessage = new ChatRoomMessage(chatUser.getNickName(), message, chatroom.getName());

        var chatRoomOwnerId = chatroom.getOwnerId();

        var ownerContact = chatUser.getContactByKademliaId(chatRoomOwnerId);

        chatUser.sendMessage(chatRoomMessage, ownerContact, true);


        return true;
    }


    public static ChatRoom processFindChatroom(ChatUser chatUser, String chatRoomName) {

        chatUser.log("Find chatroom process");

        var result = chatUser.lookupChatRoomByName(chatRoomName);

        return result;
    }


}
