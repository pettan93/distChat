package distChat.mySimuluations;

import kademlia.JKademliaNode;
import kademlia.node.KademliaId;

import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class Simulation1 {

    static int simTime = 0;

    public static void main(String[] args) throws IOException {

        JKademliaNode kad1 = new JKademliaNode("JoshuaK", new KademliaId("ASF456789djem45674DH"), 12049);
        JKademliaNode kad2 = new JKademliaNode("Crystal", new KademliaId("AJDHR678947584567464"), 4585);

        kad2.bootstrap(kad1.getNode());



    }


}
