package distChat.factory;

import distChat.model.ChatUser;
import kademlia.JKademliaNode;
import kademlia.node.KademliaId;
import org.apache.commons.codec.digest.DigestUtils;
import org.kohsuke.randname.RandomNameGenerator;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

public class ChatNodeBuilder {

    private InetAddress inetAddress;

    private Integer portPoolMin;

    private Integer portPoolMax;

    private Integer port;

    private Set<Integer> usedPorts = new HashSet<>();

    RandomNameGenerator rnd = new RandomNameGenerator(500);

    /*****************/

    private String nickName;


    public ChatNodeBuilder(String inetAddress, Integer portPoolMin, Integer portPoolMax) {
        try {
            this.inetAddress = InetAddress.getByName(inetAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.portPoolMin = portPoolMin;
        this.portPoolMax = portPoolMax;
    }

    public ChatNodeBuilder setNickName(String name) {
        this.nickName = name;
        return this;
    }

    public ChatNodeBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    public ChatUser build() {

        if (this.nickName == null) {
            this.nickName = getRandomNickName();
        }

        if (this.port == null) {
            this.port = getRandomUnusedPort();
        }

        try {
            ChatUser chatUser = new ChatUser(
                    this.nickName,
                    new JKademliaNode(
                            this.nickName,
                            new KademliaId(DigestUtils.sha1(this.nickName)),
                            this.port
                    ));

            chatUser.getKadNode().setChatUser(chatUser);
            return chatUser;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void reset() {
        this.nickName = null;
    }

    private Integer getRandomUnusedPort() {
        int r;
        do {
            r = (int) (Math.random() * (portPoolMax - portPoolMin)) + portPoolMin;
        } while (usedPorts.contains(r));

        usedPorts.add(r);

        return r;
    }

    private String getRandomNickName() {
        return rnd.next();
    }


}
