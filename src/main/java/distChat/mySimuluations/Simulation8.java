package distChat.mySimuluations;

import distChat.UI.UIController;
import distChat.factory.ChatNodeBuilder;
import distChat.model.ChatRoom;
import distChat.model.ChatRoomMessage;
import distChat.model.ChatRoomParticipant;
import distChat.model.ChatUser;

import java.io.IOException;
import java.util.Arrays;

public class Simulation8 {

    public static ChatNodeBuilder chatNodeBuilder = new ChatNodeBuilder(
            "192.168.137.107",
            7000,
            8000);

    public static void main(String[] args) throws IOException {


        ChatUser user1 = chatNodeBuilder.setNickName("Petr").setPort(7001).build();
        ChatUser user2 = chatNodeBuilder.setNickName("Anna").setPort(7002).build();
        ChatUser user3 = chatNodeBuilder.setNickName("Jan").setPort(7003).build();
        ChatUser user4 = chatNodeBuilder.setNickName("Klara").setPort(7004).build();
        ChatUser user5 = chatNodeBuilder.setNickName("Artur").setPort(7005).build();
        ChatUser user6 = chatNodeBuilder.setNickName("Petra").setPort(7006).build();
        ChatUser user7 = chatNodeBuilder.setNickName("Filip").setPort(7007).build();


        user2.bootstrap(user1);
        user3.bootstrap(user1);

        user4.bootstrap(user2);
        user5.bootstrap(user2);

        user6.bootstrap(user5);
        user7.bootstrap(user5);

        ChatRoom chatRoom = new ChatRoom("Football", user1);
        chatRoom.addMessage(new ChatRoomMessage(user1.getNickName(), "First message!"));
        user1.storeChatroom(chatRoom,true);

        user2.joinChatroom(new ChatRoomParticipant(user2),chatRoom.getName(),user2.getContactByKademliaId(chatRoom.getOwnerId()));

        user3.joinChatroom(new ChatRoomParticipant(user3),chatRoom.getName(),user3.getContactByKademliaId(chatRoom.getOwnerId()));



        UIController.buildUserController(Arrays.asList(user1, user2, user3, user4, user5, user6, user7));
        UIController.initManager();


        SimulationUtils.justWait(1);

        user1.shutdown();

        SimulationUtils.justWait(2);

        var ownerContact = user2.getContactByKademliaId(chatRoom.getOwnerId());
        user2.sendMessage(new ChatRoomMessage(user2.getNickName(), "Alive?!"),chatRoom.getName(),ownerContact,true);

    }

}
