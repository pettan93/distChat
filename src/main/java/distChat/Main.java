package distChat;


import distChat.UI.UIController;
import distChat.factory.ChatNodeBuilder;
import distChat.model.ChatUser;
import io.javalin.Javalin;

import java.io.IOException;
import java.util.*;

public class Main {

    public static ChatNodeBuilder chatNodeBuilder;


    public static void main(String[] args) {
        String ip;
        Integer nodesCount;

        try {
            ip = args[0];
            nodesCount = Integer.parseInt(args[1]);
        } catch (Exception e) {
            System.out.println("Bad program arguments");
            return;
        }

        chatNodeBuilder = new ChatNodeBuilder(
                ip,
                7000,
                8000);

        List<ChatUser> nodes = new ArrayList<>();

        ChatUser rootNode = null;

        for (int i = 0; i < nodesCount; i++) {
            chatNodeBuilder.reset();
            ChatUser nextNode = chatNodeBuilder.build();
            if (rootNode == null) {
                rootNode = nextNode;
            } else {
                nextNode.bootstrap(rootNode);
            }


            nodes.add(nextNode);
        }

        UIController.buildUserController(nodes);
        UIController.initManager();


    }


}
