package distChat.mySimuluations;

import distChat.factory.ChatNodeBuilder;
import distChat.model.ChatRoom;
import distChat.model.ChatUser;

import java.io.IOException;

public class Simulation5 {

    public static ChatNodeBuilder chatNodeBuilder = new ChatNodeBuilder(
            "192.168.137.107",
            7000,
            8000);


    public static void main(String[] args) throws IOException {



        ChatUser user1 = chatNodeBuilder.setNickName("Petr").build();

        ChatUser user2 = chatNodeBuilder.setNickName("Anna").build();

        ChatRoom chatRoom = new ChatRoom("Football", user1);
        user1.storeChatroom(chatRoom);












    }
}
