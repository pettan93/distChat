package distChat.mySimuluations;

import distChat.UI.UIController;
import distChat.factory.ChatNodeBuilder;
import distChat.model.ChatRoom;
import distChat.model.ChatUser;
import distChat.operation.NodeSearchOperation;
import kademlia.node.KademliaId;
import kademlia.operation.NodeLookupOperation;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.util.Arrays;

public class SimulationLookupUsers {

    public static ChatNodeBuilder chatNodeBuilder = new ChatNodeBuilder(
            "192.168.137.107",
            7000,
            8000);

    public static void main(String[] args) throws IOException {


        ChatUser user1 = chatNodeBuilder.setNickName("Petr").build();
        ChatUser user2 = chatNodeBuilder.setNickName("Anna").build();
        ChatUser user3 = chatNodeBuilder.setNickName("Annicka").build();
        ChatUser user4 = chatNodeBuilder.setNickName("Annna2").build();
        ChatUser user5 = chatNodeBuilder.setNickName("Annous").build();

        for(int i=0;i<20;i++){
            var nextUser = chatNodeBuilder.build();
            nextUser.bootstrap(user1);
        }

        user2.bootstrap(user1);
        user3.bootstrap(user1);
        user4.bootstrap(user1);
        user5.bootstrap(user1);

        System.out.println("---");

        SimulationUtils.justWait(2);

        var lookupId = new KademliaId(DigestUtils.sha1("Anna"));


        var operation = new NodeSearchOperation(
                user1.getKadNode().getServer(),
                user1.getKadNode(),
                lookupId,user1.getKadNode().getCurrentConfiguration());
        operation.execute();

        var result = operation.getClosestNodes();




        System.out.println(result.size());
        System.out.println(result);

//
//        UIController.buildUserController(Arrays.asList(user1, user2));
//        UIController.initManager();



    }

}
