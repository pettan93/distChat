package distChat.comm;

import com.google.gson.Gson;
import distChat.model.ChatroomUpdateContent;
import kademlia.message.Message;
import kademlia.node.Node;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MyNameIsMessage implements Message {


    private Node origin;

    private String nickname;

    public static final byte CODE = 0x60;

    public MyNameIsMessage(Node origin, String nickname) {
        this.origin = origin;
        this.nickname = nickname;
    }

    public MyNameIsMessage(DataInputStream in) throws IOException {
        this.fromStream(in);
    }

    @Override
    public final void fromStream(DataInputStream in) throws IOException {
        this.origin = new Node(in);

        byte[] buff = new byte[in.readInt()];
        in.readFully(buff);
        this.nickname = new String(buff);

    }

    @Override
    public void toStream(DataOutputStream out) throws IOException {
        origin.toStream(out);
        out.writeInt(this.nickname.length());
        out.writeBytes(this.nickname);
    }

    public Node getOrigin() {
        return this.origin;
    }

    public String getNickname() {
        return nickname;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }


    public static MyNameIsMessage fromJson(String data) {
        Gson gson = new Gson();
        return gson.fromJson(data, MyNameIsMessage.class);
    }

    @Override
    public byte code() {
        return CODE;
    }

    @Override
    public String toString() {
        return "MyNameIsMessage[origin=" + origin.getNodeId() + "," + this.nickname + "]";
    }

}
