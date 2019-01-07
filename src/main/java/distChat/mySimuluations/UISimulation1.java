package distChat.mySimuluations;

import distChat.UI.UIController;
import distChat.factory.ChatNodeBuilder;
import distChat.model.ChatUser;

public class UISimulation1 {

    public static ChatNodeBuilder chatNodeBuilder = new ChatNodeBuilder(
            "192.168.137.107",
            7000,
            8000);

    public static void main(String[] args) {

        ChatUser user1 = chatNodeBuilder.setNickName("Petr").build();
        ChatUser user2 = chatNodeBuilder.setNickName("Anna").build();
        ChatUser user3 = chatNodeBuilder.setNickName("Jan").build();
        ChatUser user4 = chatNodeBuilder.setNickName("Klara").build();

        UIController.buildUserController(user1);
        UIController.buildUserController(user2);
        UIController.buildUserController(user3);
        UIController.buildUserController(user4);


        UIController.initManager();

    }




}
