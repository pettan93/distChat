package distChat.mySimuluations;



import kademlia.JKademliaNode;
import kademlia.node.KademliaId;
import kademlia.node.Node;
import kademlia.simulations.DHTContentImpl;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Simulation1 {


    /**
     * Ulozeni contentu do lokalni node
     * Pripojeni k jinemu pc
     */

    // main for computer 1
    public static void main(String[] args) throws IOException {

//        MyClientSetup.MY_IP = "192.168.137.107";
//
        JKademliaNode kad1 = new JKademliaNode("JoshuaK", new KademliaId("ASF45678947584567467"), 7001);
        System.out.println(kad1.getNode().getSocketAddress());

        DHTContentImpl content = new DHTContentImpl(kad1.getOwnerId(), "hovno");
        kad1.put(content);


        KademliaId id = new KademliaId("ASERTKJDHGVHERJHGFLK");
        InetAddress ip = InetAddress.getByName("192.168.137.1");
        Node kad2 = new Node(id, ip, 6001);
        kad1.bootstrap(kad2);






        SimulationUtils.printNodesLoop(Arrays.asList(kad1), 5);

    }


    // main for computer 2
//    public static void main(String[] args) throws IOException {
//
//        MyClientSetup.MY_IP = "192.168.137.1";
//
//        JKademliaNode kad2 = new JKademliaNode("Crystal", new KademliaId("ASERTKJDHGVHERJHGFLK"), 6001);
//        System.out.println(kad2.getNode().getSocketAddress());
//
//        printNodesLoop(Arrays.asList(kad2), 5);
//
//    }






}
