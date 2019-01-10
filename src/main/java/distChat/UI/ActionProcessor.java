package distChat.UI;

import distChat.model.ChatRoom;
import distChat.model.ChatRoomMessage;
import distChat.model.ChatRoomParticipant;
import distChat.model.ChatUser;

public class ActionProcessor {

    public static boolean processSendMessage(ChatUser chatUser, String chatRoomName, String message) {

        chatUser.log("Send msg process");

        var chatroom = chatUser.getInvolvedChatroomByName(chatRoomName);

        var chatRoomMessage = new ChatRoomMessage(chatUser.getNickName(), message);

        var chatRoomOwnerId = chatroom.getOwnerId();

        var ownerContact = chatUser.getContactByKademliaId(chatRoomOwnerId);

        chatUser.sendMessage(chatRoomMessage, chatRoomName, ownerContact, true);

        return true;
    }


    public static ChatRoom processFindChatroom(ChatUser chatUser, String chatRoomName) {

        chatUser.log("Find chatroom process ["+chatRoomName+"]");

        var result = chatUser.lookupChatRoomByName(chatRoomName);

        return result;
    }

    public static void processJoinChatroom(ChatUser chatUser, String chatRoomName) {

        chatUser.log("Join chatroom process to [" + chatRoomName+ "]");

        var chatRoom = chatUser.lookupChatRoomByName(chatRoomName);

        var chatRoomOwnerId = chatRoom.getOwnerId();

        var ownerContact = chatUser.getContactByKademliaId(chatRoomOwnerId);

        chatUser.joinChatroom(new ChatRoomParticipant(chatUser),chatRoomName,ownerContact);
    }


}
