package distChat.mySimuluations;

import distChat.UI.UIController;
import distChat.factory.ChatNodeBuilder;
import distChat.model.ChatRoom;
import distChat.model.ChatUser;

import java.io.IOException;
import java.util.Arrays;

public class Simulation5 {

    public static ChatNodeBuilder chatNodeBuilder = new ChatNodeBuilder(
            "192.168.137.107",
            7000,
            8000);


    public static void main(String[] args) throws IOException {



        ChatUser user1 = chatNodeBuilder.setNickName("Petr").build();

        ChatUser user2 = chatNodeBuilder.setNickName("Anna").build();


        user2.bootstrap(user1);

        ChatRoom chatRoom = new ChatRoom("Football", user1);
        user1.storeChatroom(chatRoom,true);

//
//        user1.getKadNode().shutdown(false);
//
//        user1.bootstrap(user2);

        UIController.buildUserController(Arrays.asList(user1, user2));
        UIController.initManager();






    }
}
