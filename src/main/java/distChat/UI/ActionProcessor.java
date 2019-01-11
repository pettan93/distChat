package distChat.UI;

import distChat.comm.MyNameIsReciever;
import distChat.comm.WhoAreYouMessage;
import distChat.model.*;
import distChat.operation.NodeSearchOperation;
import kademlia.node.KademliaId;
import kademlia.node.Node;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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


    public static List<ChatUserSearchResult> processSearchUsers(ChatUser chatUser, String contactName){


        chatUser.log("Process search users query [" + contactName+ "]");

        var lookupId = new KademliaId(DigestUtils.sha1(contactName));


        var operation = new NodeSearchOperation(
                chatUser.getKadNode().getServer(),
                chatUser.getKadNode(),
                lookupId,chatUser.getKadNode().getCurrentConfiguration());
        try {
            operation.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return operation.getLookupedResult(chatUser);
    }

}
