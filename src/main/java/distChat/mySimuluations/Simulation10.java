package distChat.mySimuluations;

import distChat.UI.UIController;
import distChat.factory.ChatNodeBuilder;
import distChat.model.ChatRoom;
import distChat.model.ChatRoomMessage;
import distChat.model.ChatRoomParticipant;
import distChat.model.ChatUser;

import java.io.IOException;
import java.util.Arrays;

public class Simulation10 {

    public static ChatNodeBuilder chatNodeBuilder = new ChatNodeBuilder(
            "192.168.137.107",
            7000,
            8000);

    public static void main(String[] args) throws IOException {


        ChatUser user1 = chatNodeBuilder.setNickName("Petr").setPort(7001).build();
        ChatUser user2 = chatNodeBuilder.setNickName("Anna").setPort(7002).build();

        user2.bootstrap(user1);


        ChatRoom chatRoom = new ChatRoom("Football", user1);
        chatRoom.addMessage(new ChatRoomMessage(user1.getNickName(), "First message!"));
        user1.storeChatroom(chatRoom,true);



        SimulationUtils.justWait(3);





        UIController.buildUserController(Arrays.asList(user1, user2));
        UIController.initManager();

    }

}
