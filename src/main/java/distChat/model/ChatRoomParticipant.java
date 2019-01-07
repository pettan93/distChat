package distChat.model;

import java.util.Objects;

public class ChatRoomParticipant {

    private String kademliaId;

    private String nickName;

    public ChatRoomParticipant(ChatUser owner){
        this.kademliaId = owner.getKadNode().getOwnerId();
        this.nickName = owner.getNickName();
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatRoomParticipant that = (ChatRoomParticipant) o;
        return Objects.equals(kademliaId, that.kademliaId);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
