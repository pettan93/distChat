package distChat.mySimuluations;

import distChat.factory.ChatNodeBuilder;
import distChat.model.ChatUser;
import distChat.model.ChatRoom;
import kademlia.dht.GetParameter;
import kademlia.dht.JKademliaStorageEntry;
import kademlia.exceptions.ContentNotFoundException;
import kademlia.node.KademliaId;
import kademlia.simulations.DHTContentImpl;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Simulation4 {

    public static ChatNodeBuilder chatNodeBuilder = new ChatNodeBuilder(
            "192.168.137.107",
            7000,
            8000);

    /**
     * nodes network
     * save content on node
     * retrieve from another node
     */

    public static void main(String[] args) throws IOException {


        List<ChatUser> nodes = new ArrayList<>();

        ChatUser rootUser = chatNodeBuilder.setNickName("Petr").build();

        nodes.add(rootUser);

        ChatUser lastCreatedUser = null;
        for (int i = 0; i < 10; i++) {

            chatNodeBuilder.reset();
            ChatUser nextNode = chatNodeBuilder.build();

            nextNode.bootstrap(rootUser);
            nodes.add(nextNode);
            lastCreatedUser = nextNode;
        }


        ChatRoom chatRoom = new ChatRoom("Football", rootUser.getKadNode().getOwnerId());
        rootUser.storeChatroom(chatRoom);


        SimulationUtils.justWait(2);
        SimulationUtils.printChatUsers(nodes);



        System.out.println("------------------------ Last created user");
        System.out.println(lastCreatedUser.getNickName());


        KademliaId chatRoomId = new KademliaId(DigestUtils.sha1("Football"));
        GetParameter gp = new GetParameter(chatRoomId, ChatRoom.TYPE);

        try {
            JKademliaStorageEntry found = lastCreatedUser.getKadNode().get(gp);

            System.out.println(found.getContentMetadata().getOwnerId());

            System.out.println(found);
        } catch (ContentNotFoundException e) {
            e.printStackTrace();
        }


    }
}
