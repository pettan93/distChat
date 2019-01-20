package distChat;

import distChat.model.ChatUser;
import kademlia.node.KademliaId;
import org.apache.commons.codec.digest.DigestUtils;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyUtils {

    public static String formatDate(Date d, String format) {
        return d != null ? new SimpleDateFormat(format).format(d) : null;
    }

    public static String nickNameToKademliaHex(String nickname){
        byte[] bytes = DigestUtils.sha1(nickname);
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }

    public static KademliaId kademliaId(String name){
        return new KademliaId(DigestUtils.sha1(name));
    }



    public static String createPrivateChatroomName(String user1, String user2){

        return user1 + " & " + user2;
//        return "bongo";

    }



}
