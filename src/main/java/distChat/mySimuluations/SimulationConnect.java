package distChat.mySimuluations;

import distChat.UI.UIController;
import distChat.factory.ChatNodeBuilder;
import distChat.model.ChatRoom;
import distChat.model.ChatRoomMessage;
import distChat.model.ChatRoomParticipant;
import distChat.model.ChatUser;
import kademlia.node.KademliaId;
import kademlia.node.Node;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;

public class SimulationConnect {

    public static ChatNodeBuilder chatNodeBuilder = new ChatNodeBuilder(
            "192.168.137.107",
            7000,
            8000);

    public static void main(String[] args) throws IOException {

        ChatUser user1 = chatNodeBuilder.setNickName("Petr").setPort(7001).build();

//        UIController.buildUserController(Arrays.asList(user1));
//        UIController.initManager();


        System.out.println(user1.getKadNode().getNode().getSocketAddress());
        System.out.println(user1.getKadNode().getOwnerId());
        System.out.println(user1.getKadNode().getNode().getNodeId());


        var id = new KademliaId(DigestUtils.sha1("Anna"));
        String remoteIp="192.168.137.1";
        InetAddress ip = InetAddress.getByName(remoteIp);
        Node target = new Node(id, ip, Integer.parseInt("7001"));

        user1.getKadNode().bootstrap(target);

    }



    /*
    public static ChatNodeBuilder chatNodeBuilder = new ChatNodeBuilder(
            "192.168.137.1",
            7000,
            8000);

    public static void main(String[] args) throws IOException {

        ChatUser user1 = chatNodeBuilder.setNickName("Anna").setPort(7001).build();

        System.out.println(user1.getKadNode().getNode().getSocketAddress());
        System.out.println(user1.getKadNode().getOwnerId());
        System.out.println(user1.getKadNode().getNode().getNodeId());

        UIController.buildUserController(Arrays.asList(user1));
        UIController.initManager();

    }
    */

}
