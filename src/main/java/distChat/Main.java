package distChat;


import kademlia.JKademliaNode;
import kademlia.node.KademliaId;

import java.io.IOException;

public class Main {


    public static void main(String[] args) throws IOException {

        JKademliaNode kad1 = new JKademliaNode("JoshuaK", new KademliaId("ASF45678947584567467"), 7001);
        System.out.println(kad1.getNode().getSocketAddress());

//
//        JKademliaNode kad2 = new JKademliaNode("Crystal", new KademliaId("ASERTKJDHGVHERJHGFLK"), 6001);
//
//        kad2.bootstrap(kad1.getNode());


//        KademliaId id = new KademliaId("ASERTKJDHGVHERJHGFLK");
//        InetAddress ip =InetAddress.getByName("192.168.137.107");
//        Node kad2 = new Node(id,ip,6001);
//        kad1.bootstrap(kad2);


//        System.out.println(InetAddress.getLocalHost());
//
//        System.out.println(InetAddress.getByName("192.168.137.107").toString());

//        Enumeration Interfaces = NetworkInterface.getNetworkInterfaces();
//        while(Interfaces.hasMoreElements())
//        {
//            NetworkInterface Interface = (NetworkInterface)Interfaces.nextElement();
//            Enumeration Addresses = Interface.getInetAddresses();
//            while(Addresses.hasMoreElements())
//            {
//                InetAddress Address = (InetAddress)Addresses.nextElement();
//                System.out.println(Address.getHostAddress());
//            }
//        }




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
