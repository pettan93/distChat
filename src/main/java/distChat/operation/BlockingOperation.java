package distChat.operation;

import kademlia.exceptions.RoutingException;

import java.io.IOException;

public interface BlockingOperation {

    void block();

    void release();

}
