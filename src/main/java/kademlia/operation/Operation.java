package kademlia.operation;

import distChat.operation.RepeatOperation;
import kademlia.exceptions.RoutingException;

import java.io.IOException;

/**
 * An operation in the Kademlia routing protocol
 *
 * @author Joshua Kissoon
 * @created 20140218
 */
public interface Operation
{

    /**
     * Starts an operation and returns when the operation is finished
     *
     * @throws kademlia.exceptions.RoutingException
     */
    public void execute() throws IOException, RoutingException;
}
