package distChat.mySimuluations;



import kademlia.JKademliaNode;
import kademlia.node.KademliaId;
import kademlia.node.Node;
import kademlia.simulations.DHTContentImpl;
import org.apache.commons.codec.digest.DigestUtils;
import org.kohsuke.randname.RandomNameGenerator;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;

public class Simulation3 {



    public static void main(String[] args) throws IOException {

        RandomNameGenerator rnd = new RandomNameGenerator(500);

        for(int i = 0;i<1000;i++){
            var kad1 = new KademliaId();
            var kad2 = new KademliaId();

            System.out.println(kad1.getDistance(kad2));
        }


//        SimulationUtils.printNodesLoop(Arrays.asList(kad1), 5);

    }







}
