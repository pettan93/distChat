package distChat.comm;

import distChat.model.ChatRoom;
import distChat.model.ChatRoomMessage;
import distChat.model.ChatRoomParticipant;
import distChat.model.ChatroomUpdateContent;
import kademlia.message.Message;
import kademlia.node.Node;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class JoinPrivateChatMessage implements Message, Serializable {

    public static final byte CODE = 0x45;

    private Node origin;

    private String chatroomname;

    public JoinPrivateChatMessage(String chatroomname, Node origin) {
        this.origin = origin;
        this.chatroomname = chatroomname;
   }

    public JoinPrivateChatMessage(DataInputStream in) throws IOException {
        this.fromStream(in);
    }

    public String getContent() {
        return chatroomname;
    }

    public Node getOrigin() {
        return this.origin;
    }

    @Override
    public byte code() {
        return CODE;
    }

    @Override
    public final void fromStream(DataInputStream in) throws IOException {
        this.origin = new Node(in);

        byte[] buff = new byte[in.readInt()];
        in.readFully(buff);
        this.chatroomname = new String(buff);

    }

    @Override
    public void toStream(DataOutputStream out) throws IOException {
        origin.toStream(out);
        out.writeInt(this.chatroomname.length());
        out.writeBytes(this.chatroomname);
    }


    @Override
    public String toString() {
        return "Origin["+origin.getNodeId()+"] "+ this.chatroomname;
    }


}
