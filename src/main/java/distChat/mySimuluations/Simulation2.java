package distChat.mySimuluations;

import distChat.factory.ChatNodeBuilder;
import distChat.model.ChatUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Simulation2 {

    public static ChatNodeBuilder chatNodeBuilder = new ChatNodeBuilder(
            "192.168.137.107",
            7000,
            8000);


    public static void main(String[] args) throws IOException {


        List<ChatUser> nodes = new ArrayList<>();

        ChatUser rootNode = chatNodeBuilder.setNickName("Petr").build();

        nodes.add(rootNode);

        for(int i = 0;i<10;i++){

            chatNodeBuilder.reset();
            ChatUser nextNode = chatNodeBuilder.build();

            nextNode.bootstrap(rootNode);
            nodes.add(nextNode);
        }





//        ChatRoom chatRoom = new ChatRoom("Football", kad1.getOwnerId());
//        int storedOnNodes = kad1.put(chatRoom);
//
//
//        kad2.bootstrap(kad1.getNode());
//        kad3.bootstrap(kad2.getNode());
//
//
//
        SimulationUtils.printChatUsersLoop(nodes, 5);



    }
}
