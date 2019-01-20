package distChat.UI;

import distChat.MyUtils;
import distChat.model.*;
import distChat.operation.NodeSearchOperation;
import kademlia.node.KademliaId;
import kademlia.node.Node;
import kademlia.operation.NodeLookupOperation;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.util.List;

public class ActionProcessor {

    public static boolean processSendMessage(ChatUser chatUser, String chatRoomName, String message) {

        chatUser.log("Send msg process");

        var chatroom = chatUser.getInvolvedChatroomByName(chatRoomName);

        var chatRoomMessage = new ChatRoomMessage(chatUser.getNickName(), message);

        var chatRoomOwnerId = chatroom.getOwnerId();

        var ownerContact = chatUser.getStoredContactByKademliaId(chatRoomOwnerId);

        chatUser.sendChatRoomMessage(chatRoomMessage, chatRoomName, ownerContact, true);

        return true;
    }


    public static ChatRoom processFindChatroom(ChatUser chatUser, String chatRoomName) {

        chatUser.log("Find chatroom process [" + chatRoomName + "]");

        var result = chatUser.lookupChatRoomByName(chatRoomName);

        return result;
    }

    public static void processJoinChatroom(ChatUser me, String chatRoomName) {

        me.log("Join chatroom process to [" + chatRoomName + "]");

        var chatRoom = me.lookupChatRoomByName(chatRoomName);

        var chatRoomOwnerId = chatRoom.getOwnerId();

        var ownerContact = me.getStoredContactByKademliaId(chatRoomOwnerId);

        if (ownerContact != null) {
            me.joinChatroom(new ChatRoomParticipant(me), chatRoomName, ownerContact);
        } else {
            me.log("I dont have contact for chatroom owner!");
            NodeLookupOperation ndlo = new NodeLookupOperation(
                    me.getKadNode().getServer(),
                    me.getKadNode(),
                    MyUtils.kademliaId(chatRoomOwnerId),
                    me.getKadNode().getCurrentConfiguration());
            try {
                ndlo.execute();
                ownerContact = me.getStoredContactByKademliaId(chatRoomOwnerId);
                me.joinChatroom(new ChatRoomParticipant(me), chatRoomName, ownerContact);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public static List<ChatUserSearchResult> processSearchUsers(ChatUser chatUser, String contactName) {


        chatUser.log("Process search users query [" + contactName + "]");

        var lookupId = new KademliaId(DigestUtils.sha1(contactName));


        var operation = new NodeSearchOperation(
                chatUser.getKadNode().getServer(),
                chatUser.getKadNode(),
                lookupId, chatUser.getKadNode().getCurrentConfiguration());
        try {
            operation.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return operation.getLookupedResult(chatUser);
    }


    public static void processInviteUser(ChatUser me, String inviteKademliaId, String chatRoomName) {

        var inviteContact = me.getStoredContactByKademliaId(inviteKademliaId);

        if (inviteContact != null) {
            me.inviteToPrivateChat(inviteContact, chatRoomName);
        } else {
            me.log("I dont have contact for chatroom owner!");
            NodeLookupOperation ndlo = new NodeLookupOperation(
                    me.getKadNode().getServer(),
                    me.getKadNode(),
                    MyUtils.kademliaId(inviteKademliaId),
                    me.getKadNode().getCurrentConfiguration());
            try {
                ndlo.execute();
                inviteContact = me.getStoredContactByKademliaId(inviteKademliaId);
                me.inviteToPrivateChat(inviteContact, chatRoomName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

}
