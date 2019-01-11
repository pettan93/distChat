package distChat.mySimuluations;

import distChat.UI.UIController;
import distChat.comm.MyNameIsReciever;
import distChat.comm.WhoAreYouMessage;
import distChat.comm.WhoAreYouReciever;
import distChat.factory.ChatNodeBuilder;
import distChat.model.ChatRoom;
import distChat.model.ChatRoomMessage;
import distChat.model.ChatRoomParticipant;
import distChat.model.ChatUser;

import java.io.IOException;
import java.util.Arrays;

public class SimulationWhoAreYou {

    public static ChatNodeBuilder chatNodeBuilder = new ChatNodeBuilder(
            "192.168.137.107",
            7000,
            8000);

    public static void main(String[] args) throws IOException {


        ChatUser user1 = chatNodeBuilder.setNickName("Petr").setPort(7001).build();
        ChatUser user2 = chatNodeBuilder.setNickName("Anna").setPort(7002).build();


        var reciever = new MyNameIsReciever(user1.getKadNode());
        user1.getKadNode().getServer().sendMessage(
                user2.getKadNode().getNode(),
                new WhoAreYouMessage(user1.getKadNode().getNode()),
                reciever);
        System.out.println(reciever.getResult());




    }

}
