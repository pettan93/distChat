package distChat.mySimuluations;

import distChat.factory.ChatNodeBuilder;
import distChat.model.ChatRoom;
import distChat.model.ChatUser;

import java.io.IOException;

public class SimulationLookupChatroom {

    public static ChatNodeBuilder chatNodeBuilder = new ChatNodeBuilder(
            "192.168.137.107",
            7000,
            8000);

    public static void main(String[] args) throws IOException {


        ChatUser user1 = chatNodeBuilder.setNickName("Petr").build();

        ChatUser user2 = chatNodeBuilder.setNickName("Anna").build();

        user2.bootstrap(user1);


        ChatRoom chatRoom = new ChatRoom("Football", user1);
        user1.storeChatroom(chatRoom);


//        System.out.println(user2);
        user2.lookupChatRoomByName("Football");
//        System.out.println(user2);


    }

}
