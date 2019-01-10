package distChat.comm;

import distChat.model.ChatUser;
import kademlia.KademliaNode;
import kademlia.message.AcknowledgeMessage;
import kademlia.message.Message;
import kademlia.message.Receiver;

import java.io.IOException;

public class AreYouAwakeConfirmReciever implements Receiver {


    // TODO ZMENIT NA OPERATION TIME
    private static final int MAX_RESPONSE_WAIT = 2000;

    private Boolean result = false;

    public AreYouAwakeConfirmReciever() {
    }

    @Override
    public void receive(Message incoming, int commId) {
        this.result = true;
    }

    @Override
    public void timeout(int conversationId) throws IOException {

    }

    public synchronized Boolean  getResult() {
        int totalTimeWaited = 0;
        int timeInterval = 50;     // We re-check every x milliseconds
        while (totalTimeWaited < MAX_RESPONSE_WAIT) {
            if (!result) {
                try {
                    wait(timeInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                totalTimeWaited += timeInterval;
            } else {
                return result;
            }
        }
        return false;

    }
}
