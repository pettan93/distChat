package distChat;


import kademlia.JKademliaNode;
import kademlia.node.KademliaId;
import kademlia.simulations.DHTContentImpl;

import java.io.IOException;

public class Main {


    public static void main(String[] args) throws IOException {

        JKademliaNode kad1 = new JKademliaNode("JoshuaK", new KademliaId("ASF45678947584567467"), 7574);



        JKademliaNode kad2 = new JKademliaNode("Crystal", new KademliaId("ASERTKJDHGVHERJHGFLK"), 7572);


        kad2.bootstrap(kad1.getNode());





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
