package distChat.operation;

import kademlia.exceptions.RoutingException;

import java.io.IOException;

public interface BlockingOperation {

    public void block();

    public void release();

}
