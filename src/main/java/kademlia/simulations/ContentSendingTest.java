package kademlia.simulations;

import kademlia.JKademliaNode;
import kademlia.dht.GetParameter;
import kademlia.dht.KademliaStorageEntry;
import kademlia.exceptions.ContentNotFoundException;
import kademlia.node.KademliaId;

import java.io.IOException;
import java.util.UUID;

/**
 * Testing sending and receiving content between 2 Nodes on a network
 *
 * @author Joshua Kissoon
 * @since 20140224
 */
public class ContentSendingTest
{

    public static void main(String[] args)
    {
        try
        {
            /* Setting up 2 Kad networks */
            JKademliaNode kad1 = new JKademliaNode("JoshuaK", new KademliaId("ASF45678947584567467"), 7574);
            System.out.println("Created Node Kad 1: " + kad1.getNode().getNodeId());

            JKademliaNode kad2 = new JKademliaNode("Crystal", new KademliaId("ASERTKJDHGVHERJHGFLK"), 7572);
            System.out.println("Created Node Kad 2: " + kad2.getNode().getNodeId());


            kad2.bootstrap(kad1.getNode());



            /**
             * Lets create the content and share it
             * content owner is kad2 (Crystal)
             */
            String data = "";
            for (int i = 0; i < 500; i++)
            {
                data += UUID.randomUUID();
            }
            System.out.println(data);
            DHTContentImpl content = new DHTContentImpl(kad2.getOwnerId(), data);
            kad2.put(content);




            /**
             * Lets retrieve the content from kad2
             */
            System.out.println("Retrieving Content");
            GetParameter gp = new GetParameter(content.getKey(), DHTContentImpl.TYPE);
            gp.setOwnerId(content.getOwnerId());
            System.out.println("Get Parameter: " + gp);
            KademliaStorageEntry conte = kad2.get(gp);
            System.out.println("Content Found: " + new DHTContentImpl().fromSerializedForm(conte.getContent()));
            System.out.println("Content Metadata: " + conte.getContentMetadata());

        }
        catch (IOException | ContentNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
