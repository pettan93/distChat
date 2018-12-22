package distChat.comm;

import distChat.model.ChatRoomMessage;
import kademlia.message.Message;
import kademlia.node.Node;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class StoreMsgReqMessage implements Message, Serializable {

    public static final byte CODE = 0x50;

    private ChatRoomMessage content;

    public StoreMsgReqMessage(ChatRoomMessage content)
    {
        this.content = content;
    }


    public StoreMsgReqMessage(DataInputStream in)
    {
        this.fromStream(in);
    }


    @Override
    public byte code()
    {
        return CODE;
    }

    @Override
    public void toStream(DataOutputStream out)
    {
        try
        {
            out.writeInt(this.content.toJson().length());

            out.writeBytes(this.content.toJson());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public final void fromStream(DataInputStream in)
    {
        try
        {
            byte[] buff = new byte[in.readInt()];
            in.readFully(buff);
            this.content = ChatRoomMessage.fromJson(new String(buff));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String toString()
    {
        return this.content.toString();
    }


}
