package distChat;


import kademlia.JKademliaNode;
import kademlia.node.KademliaId;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Main {


    public static void main(String[] args) throws IOException {

        JKademliaNode kad1 = new JKademliaNode("JoshuaK", new KademliaId("ASF45678947584567467"), 7001);
//
//        JKademliaNode kad2 = new JKademliaNode("Crystal", new KademliaId("ASERTKJDHGVHERJHGFLK"), 7002);
//
//        kad2.bootstrap(kad1.getNode());


        System.out.println(InetAddress.getLocalHost());

        InetAddress inetAddress = InetAddress.getByName("localhost");

        System.out.println(inetAddress);





    }


//    public static void main(String[] args) {
//        Controller controller = new Controller();
//
//        Javalin app = Javalin.create()
//                .enableStaticFiles("/public")
//                .enableRouteOverview("/path")
//                .start(8080);
//
//        app.get("/interface", ctx -> {
//            Map<String, Object> model = new HashMap<>();
//            ctx.render("public/interface.vm", model);
//        });
//
//        app.get("/manager", ctx -> {
//            Map<String, Object> model = new HashMap<>();
//            ctx.render("public/manager.vm", model);
//        });
//    }


}
