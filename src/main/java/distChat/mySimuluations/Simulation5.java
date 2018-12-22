package distChat.mySimuluations;

import distChat.comm.ChatroomUpdateMessageReciever;
import distChat.comm.StoreMsgReqMessage;
import distChat.comm.StoreMsgReqReciever;
import distChat.factory.ChatNodeBuilder;
import distChat.model.ChatRoomMessage;
import distChat.model.ChatUser;

import java.io.IOException;
import java.util.Date;

public class Simulation5 {

    public static ChatNodeBuilder chatNodeBuilder = new ChatNodeBuilder(
            "192.168.137.107",
            7000,
            8000);


    public static void main(String[] args) throws IOException {



        ChatUser user1 = chatNodeBuilder.setNickName("Petr").build();

        ChatUser user2 = chatNodeBuilder.setNickName("Anna").build();

        user1.getKadNode().getServer().sendMessage(
                user2.getKadNode().getNode(),
                new StoreMsgReqMessage(new ChatRoomMessage("Anna","ahoj")),
                null);






    }
}
