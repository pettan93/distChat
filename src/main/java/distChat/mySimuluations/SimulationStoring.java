package distChat.mySimuluations;

import distChat.factory.ChatNodeBuilder;
import distChat.model.ChatRoom;
import distChat.model.ChatRoomMessage;
import distChat.model.ChatUser;

import java.io.IOException;

public class SimulationStoring {

    public static ChatNodeBuilder chatNodeBuilder = new ChatNodeBuilder(
            "192.168.137.107",
            7000,
            8000);

    public static void main(String[] args) throws IOException {


        ChatUser user1 = chatNodeBuilder.setNickName("Petr").build();
        ChatUser user2 = chatNodeBuilder.setNickName("Anna").build();
        ChatUser user3 = chatNodeBuilder.setNickName("Jan").build();
        ChatUser user4 = chatNodeBuilder.setNickName("Klara").build();
        ChatUser user5 = chatNodeBuilder.setNickName("Artur").build();
        ChatUser user6 = chatNodeBuilder.setNickName("Petra").build();
        ChatUser user7 = chatNodeBuilder.setNickName("Filip").build();


        user2.bootstrap(user1);
        user3.bootstrap(user1);

        user4.bootstrap(user2);
        user5.bootstrap(user2);

        user6.bootstrap(user5);
        user7.bootstrap(user5);

        ChatRoom chatRoom = new ChatRoom("Football", user1);
        user7.storeChatroom(chatRoom,true);

        SimulationUtils.justWait(2);

        var chatRoomArtur = user5.getStoredChatroom("Football");


        chatRoomArtur.addMessage(new ChatRoomMessage("Artur","ahoj"));

        user5.storeChatroom(chatRoomArtur,true);


        SimulationUtils.justWait(2);

//        SimulationUtils.printChatUsers(Arrays.asList(user1,user2,user3,user4,user5,user6,user7));


        System.out.println(user5);
        System.out.println(user5.getStoredChatroom("Football"));

        System.out.println(user3);
        System.out.println(user3.getStoredChatroom("Football"));

    }

}
