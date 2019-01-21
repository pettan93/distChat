package distChat.operation;

import distChat.model.ChatUser;
import kademlia.exceptions.RoutingException;
import kademlia.operation.Operation;

import java.io.IOException;

// operation which stores chatroom to kademlia network
public class OwnerBackupOperation implements Operation {

    private ChatUser me;


    public OwnerBackupOperation(ChatUser me) {
        this.me = me;
    }

    @Override
    public void execute() throws IOException, RoutingException {




        for (String s : me.getChatRoomsOwned().keySet()) {
            me.log("backuping chatroom ["+s+"]");

            me.storeChatroom(me.getChatRoomsOwned().get(s),false);


        }


    }



}
