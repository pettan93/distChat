package distChat.comm;

import distChat.model.ChatRoomMessage;
import kademlia.message.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class ChatRoomUpdateMessage implements Message, Serializable {

    public static final byte CODE = 0x51;

    private String content = "OK";

    public ChatRoomUpdateMessage()
    {
    }


    public ChatRoomUpdateMessage(DataInputStream in)
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
            out.writeInt(this.content.length());
            out.writeBytes(this.content);
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
            this.content = new String(buff);
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
