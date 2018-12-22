package distChat.model;

import kademlia.routing.Contact;

public interface IChatUser {

    void storeChatroom(ChatRoom chatRoom);

    Contact lookupUserByName(String userName);

    ChatRoom lookupChatRoomByName(String chatRoomName);

    void sendMessage(ChatRoomMessage message, ChatRoom chatRoom);


}
