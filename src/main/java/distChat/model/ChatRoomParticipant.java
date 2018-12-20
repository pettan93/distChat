package distChat.model;

public class ChatRoomParticipant {

    private String kademliaId;

    private String nickName;

    public ChatRoomParticipant(String kademliaId, String nickName) {
        this.kademliaId = kademliaId;
        this.nickName = nickName;
    }

    public String getKademliaId() {
        return kademliaId;
    }

    public void setKademliaId(String kademliaId) {
        this.kademliaId = kademliaId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
