package distChat.comm;

import distChat.model.ChatRoomMessage;
import distChat.model.ChatRoomParticipant;
import distChat.model.ChatroomUpdateContent;
import kademlia.message.Message;
import kademlia.node.Node;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * Store new message to chatroom
 */
public class ChatRoomUpdateMessage implements Message, Serializable {

    public static final byte CODE = 0x50;

    private Node origin;

    private ChatroomUpdateContent content;

    public ChatRoomUpdateMessage(String chatRoomName, ChatRoomMessage content, Node origin) {
        this.origin = origin;
        this.content = new ChatroomUpdateContent(chatRoomName, content, null);
    }

    public ChatRoomUpdateMessage(ChatRoomUpdateMessage msg, Node origin) {
        this.origin = origin;
        this.content = msg.content;
    }

    public ChatRoomUpdateMessage(String chatRoomName, ChatRoomParticipant content, Node origin) {
        this.origin = origin;
        this.content = new ChatroomUpdateContent(chatRoomName, null, content);
    }

    public ChatRoomUpdateMessage(String chatRoomName, String newOnwer, Node origin) {
        this.origin = origin;
        this.content = new ChatroomUpdateContent(chatRoomName, newOnwer);
    }

    public ChatRoomUpdateMessage(DataInputStream in) {
        this.fromStream(in);
    }

    public ChatroomUpdateContent getContent() {
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
            this.content = ChatroomUpdateContent.fromJson(new String(buff));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Origin["+origin.getNodeId()+"] "+ this.content.toString();
    }


}
