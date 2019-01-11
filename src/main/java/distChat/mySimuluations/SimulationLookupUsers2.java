package distChat.mySimuluations;

import distChat.UI.UIController;
import distChat.factory.ChatNodeBuilder;
import distChat.model.ChatUser;
import distChat.operation.NodeSearchOperation;
import kademlia.node.KademliaId;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.util.Arrays;

public class SimulationLookupUsers2 {

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
        ChatUser user8 = chatNodeBuilder.setNickName("Anna 2").setPort(7008).build();
        ChatUser user9 = chatNodeBuilder.setNickName("Annous").setPort(7009).build();
        ChatUser user10 = chatNodeBuilder.setNickName("Anntonieta").setPort(7010).build();


        user2.bootstrap(user1);
        user3.bootstrap(user1);
        user4.bootstrap(user2);
        user5.bootstrap(user2);
        user6.bootstrap(user5);
        user7.bootstrap(user5);
        user8.bootstrap(user5);
        user9.bootstrap(user2);
        user10.bootstrap(user9);

        UIController.buildUserController(Arrays.asList(user1, user2, user3, user4, user5, user6, user7, user8, user9, user10));
        UIController.initManager();


    }

}
