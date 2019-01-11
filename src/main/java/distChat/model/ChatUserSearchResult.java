package distChat.model;

import kademlia.node.Node;

public class ChatUserSearchResult {

    private Node node;

    private String nickname;

    public ChatUserSearchResult(Node node, String nickname) {
        this.node = node;
        this.nickname = nickname;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "ChatUserSearchResult{" +
                "node=" + node +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
