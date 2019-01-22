package distChat;


import distChat.UI.UIController;
import distChat.factory.ChatNodeBuilder;
import distChat.model.ChatUser;
import io.javalin.Javalin;

import java.io.IOException;
import java.util.*;

public class Main {

    public static ChatNodeBuilder chatNodeBuilder;

    private static final String localIp = "192.168.137.107";
    private static final Integer dummy_nodes = 5;


    public static void main(String[] args) {

        chatNodeBuilder = new ChatNodeBuilder(
                localIp,
                7000,
                8000);

        List<ChatUser> nodes = new ArrayList<>();

        ChatUser rootNode = null;

        for (int i = 0; i < dummy_nodes; i++) {
            chatNodeBuilder.reset();
            ChatUser nextNode = chatNodeBuilder.build();
            if (rootNode == null) {
                rootNode = nextNode;
            } else {
                nextNode.bootstrap(rootNode);
            }


            nodes.add(nextNode);
        }

        if(nodes.size() > 0)
            UIController.buildUserController(nodes);
        UIController.initManager();


    }


}
