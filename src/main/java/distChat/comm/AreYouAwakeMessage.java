package distChat.comm;

import kademlia.message.Message;
import kademlia.node.Node;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class AreYouAwakeMessage implements Message {


    private Node origin;
    public static final byte CODE = 0x43;

    public AreYouAwakeMessage(Node origin)
    {
        this.origin = origin;
    }

    public AreYouAwakeMessage(DataInputStream in) throws IOException
    {
        this.fromStream(in);
    }

    @Override
    public final void fromStream(DataInputStream in) throws IOException
    {
        this.origin = new Node(in);
    }

    @Override
    public void toStream(DataOutputStream out) throws IOException
    {
        origin.toStream(out);
    }

    public Node getOrigin()
    {
        return this.origin;
    }

    @Override
    public byte code()
    {
        return CODE;
    }

    @Override
    public String toString()
    {
        return "AreYouAwakeMessage[origin=" + origin.getNodeId() + "]";
    }

}
