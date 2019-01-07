package distChat.mySimuluations;

import distChat.model.ChatUser;
import kademlia.JKademliaNode;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SimulationUtils {

    public static void printChatUsersLoop(List<ChatUser> nodes, int intervalSeconds) {
        printNodesLoop(nodes
                .stream()
                .filter(Objects::nonNull)
                .map(chatNode -> chatNode.getKadNode())
                .collect(Collectors.toList()), intervalSeconds);
    }

    public static void printChatUsers(List<ChatUser> nodes) {
        printNodes(nodes
                .stream()
                .filter(Objects::nonNull)
                .map(chatNode-> chatNode.getKadNode())
                .collect(Collectors.toList()));
    }

    public static void printNodesLoop(List<JKademliaNode> nodes, int intervalSeconds) {

        Timer timer = new Timer(true);
        timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {

                        printNodes(nodes);

                    }
                },
                // Delay                        // Interval
                1000, intervalSeconds * 1000
        );
    }

    public static void printNodes(List<JKademliaNode> nodes) {

        for (JKademliaNode node : nodes) {
            System.out.println(node);
            try {
                node.refresh();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void justWait(Integer seconds){
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
