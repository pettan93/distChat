package distChat.comm;

import distChat.model.ChatRoomMessage;
import kademlia.message.Message;
import kademlia.node.Node;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * Store new message to chatroom
 */
public class NewMsgReqMessage implements Message, Serializable {

    public static final byte CODE = 0x50;

    private Node origin;

    private ChatRoomMessage content;

    public NewMsgReqMessage(ChatRoomMessage content, Node origin) {
        this.origin = origin;
        this.content = content;
    }


    public NewMsgReqMessage(DataInputStream in) {
        this.fromStream(in);
    }

    public ChatRoomMessage getContent() {
        return content;
    }

    public Node getOrigin() {
        return this.origin;
    }

    @Override
    public byte code() {
        return CODE;
    }

    @Override
    public void toStream(DataOutputStream out) {
        try {
            this.origin.toStream(out);

            out.writeInt(this.content.toJson().length());

            out.writeBytes(this.content.toJson());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public final void fromStream(DataInputStream in) {
        try {
            this.origin = new Node(in);
            byte[] buff = new byte[in.readInt()];
            in.readFully(buff);
            this.content = ChatRoomMessage.fromJson(new String(buff));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return this.content.toString();
    }


}
