package distChat.UI;

import distChat.factory.ChatNodeBuilder;
import distChat.model.ChatRoom;
import distChat.model.ChatUser;
import io.javalin.Javalin;
import kademlia.JKademliaNode;
import kademlia.node.KademliaId;
import kademlia.node.Node;
import org.apache.commons.codec.digest.DigestUtils;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UIController {

    public static ArrayList<ChatUser> chatUsers = new ArrayList();


    public static void initManager() {

        Javalin app = Javalin.create()
                .enableStaticFiles("/public")
                .enableRouteOverview("/path")
                .start(5000);

        app.get("/", ctx -> {
            Map<String, Object> model = new HashMap<>();
            model.put("chatUsers", chatUsers);
            ctx.render("public/manager.vm", model);
        });

        app.get("/registrator", ctx -> {
            Map<String, Object> model = new HashMap<>();
            ctx.render("public/registrator.vm", model);
        });


        app.post("/registrator", ctx -> {

            ChatUser newUser = null;
            if (ctx.formParam("registerBootstrapNodeNickName") != null
                    && ctx.formParam("registerBootstrapNodeIpPort") != null
                    && ctx.formParam("registerNodeNickName") != null) {

                var ipAdressString = ctx.formParam("registerBootstrapNodeIpPort").split(":")[0];
                var port = ctx.formParam("registerBootstrapNodeIpPort").split(":")[1];


                try {
                    newUser = ChatNodeBuilder.instance.setNickName(ctx.formParam("registerNodeNickName")).build();
                    KademliaId id = new KademliaId(DigestUtils.sha1(ctx.formParam("registerBootstrapNodeNickName")));
                    InetAddress ip = InetAddress.getByName(ipAdressString);
                    Node target = new Node(id, ip, Integer.parseInt(port));
                    newUser.getKadNode().bootstrap(target);
                } catch (Exception e) {
                    e.printStackTrace();
                    Map<String, Object> model = new HashMap<>();
                    ctx.render("public/registrator.vm", model);
                    if (newUser != null) {
                        newUser.getKadNode().shutdown(false);
                    }
                    return;
                }


//                UIController.chatUsers.add(newUser);

                UIController.buildUserController(newUser);

                Map<String, Object> model = new HashMap<>();
                model.put("chatUser", newUser);
                ctx.redirect("http://localhost:" + newUser.getUiPort() + "/");
                ctx.render("public/interface-main.vm", model);

            }

        });

        app.get("/kill/:nickname", ctx -> {

            var chatUser = getLocalManagerChatUserByNickName(ctx.pathParam("nickname"));

            if (chatUser != null) {
                chatUser.shutdown();
            }

            ctx.redirect("/");
        });


    }

    public static void buildUserController(List<ChatUser> chatUserList) {
        for (ChatUser chatUser : chatUserList) {
            buildUserController(chatUser);
        }
    }

    public static void buildUserController(ChatUser chatUser) {

        int uiport = chatUser.getKadNode().getPort() + 1000;

        chatUser.setUiPort(uiport);

        Javalin app = Javalin.create()
                .enableStaticFiles("/public")
                .enableRouteOverview("/path")
                .start(uiport);

        app.get("/", ctx -> {

            if (!chatUser.isNodeRunning()) {
                ctx.redirect("/bootstrap");
            }

            Map<String, Object> model = new HashMap<>();
            model.put("chatUser", chatUser);
            ctx.render("public/interface-main.vm", model);
        });

        app.get("/dht", ctx -> {

            if (!chatUser.isNodeRunning()) {
                ctx.redirect("/bootstrap");
            }

            Map<String, Object> model = new HashMap<>();
            model.put("chatUser", chatUser);
            ctx.render("public/interface-dht.vm", model);
        });


        app.get("/routing", ctx -> {

            if (!chatUser.isNodeRunning()) {
                ctx.redirect("/bootstrap");
            }

            Map<String, Object> model = new HashMap<>();
            model.put("chatUser", chatUser);
            ctx.render("public/interface-routing.vm", model);
        });

        app.get("/chatroommanager", ctx -> {

            if (!chatUser.isNodeRunning()) {
                ctx.redirect("/bootstrap");
            }

            Map<String, Object> model = new HashMap<>();
            if (ctx.queryParam("chatRoomName") != null) {
                model.put("searchQuery", ctx.queryParam("chatRoomName"));
                model.put("result", ActionProcessor.processFindChatroom(chatUser, ctx.queryParam("chatRoomName")));
            }
            model.put("chatUser", chatUser);
            ctx.render("public/interface-chatroom-manager.vm", model);
        });

        app.get("/joinchatroom", ctx -> {

            if (!chatUser.isNodeRunning()) {
                ctx.redirect("/bootstrap");
            }

            Map<String, Object> model = new HashMap<>();
            if (ctx.queryParam("chatRoomName") != null) {
                ActionProcessor.processJoinChatroom(chatUser, ctx.queryParam("chatRoomName"));
            }
            model.put("chatUser", chatUser);
            ctx.redirect("/");
        });


        app.post("/chatroommanager", ctx -> {

            if (!chatUser.isNodeRunning()) {
                ctx.redirect("/bootstrap");
            }

            Map<String, Object> model = new HashMap<>();
            model.put("chatUser", chatUser);
            if (ctx.formParam("newChatroomName") != null) {

                var newChatroomName = ctx.formParam("newChatroomName");
                chatUser.log("Creating new chatroom with name [" + newChatroomName + "]");

                try {

                    ChatRoom chatRoom = new ChatRoom(newChatroomName, chatUser);
                    chatUser.storeChatroom(chatRoom,true);


                } catch (Exception e){

                    model.put("createError",true);
                    ctx.render("public/interface-chatroom-manager.vm", model);
                    e.printStackTrace();
                    return;
                }


                ctx.redirect("/chatroom/"+newChatroomName);
            }
        });


        app.get("/chatroom/:chatroomname", ctx -> {

            if (!chatUser.isNodeRunning()) {
                ctx.redirect("/bootstrap");
            }

            Map<String, Object> model = new HashMap<>();
            model.put("chatUser", chatUser);
            model.put("chatRoomName", ctx.pathParam("chatroomname"));
            ctx.render("public/interface-chatroom.vm", model);
        });

        app.post("/chatroom/:chatroomname/newmsg", ctx -> {
            if (ctx.formParam("newMessage") != null) {
                ActionProcessor.processSendMessage(chatUser, ctx.pathParam("chatroomname"), ctx.formParam("newMessage"));
            }
            ctx.redirect("/chatroom/" + ctx.pathParam("chatroomname"));
        });


        app.get("/bootstrap", ctx -> {
            if (chatUser.isNodeRunning()) {
                ctx.redirect("/");
                return;
            }
            Map<String, Object> model = new HashMap<>();
            model.put("chatUser", chatUser);
            ctx.render("public/bootstrap.vm", model);
        });

        app.get("/logout", ctx -> {
            chatUser.shutdown();
            ctx.redirect("/bootstrap");
        });


        app.post("/bootstrap", ctx -> {

            if (chatUser.isNodeRunning()) {
                ctx.redirect("/");
                return;
            }
            if (ctx.formParam("registerBootstrapNodeNickName") != null && ctx.formParam("registerBootstrapNodeIpPort") != null) {

                JKademliaNode newMyNode = new JKademliaNode(
                        chatUser.getKadNode().getOwnerId(),
                        chatUser.getKadNode().getNode().getNodeId(),
                        chatUser.getKadNode().getPort());

                try {
                    var ipAdressString = ctx.formParam("registerBootstrapNodeIpPort").split(":")[0];
                    var port = ctx.formParam("registerBootstrapNodeIpPort").split(":")[1];

                    KademliaId id = new KademliaId(DigestUtils.sha1(ctx.formParam("registerBootstrapNodeNickName")));
                    InetAddress ip = InetAddress.getByName(ipAdressString);
                    Node target = new Node(id, ip, Integer.parseInt(port));
                    newMyNode.bootstrap(target);
                } catch (Exception e) {
                    e.printStackTrace();
                    Map<String, Object> model = new HashMap<>();
                    model.put("error", true);
                    model.put("chatUser", chatUser);
                    ctx.render("public/bootstrap.vm", model);
                    newMyNode.shutdown(false);
                    return;
                }
                chatUser.reconnect(newMyNode);
            }


            ctx.redirect("/");
        });


        new UIRefresherResource(app, chatUser).buildResource();

        chatUsers.add(chatUser);
    }


    public static ChatUser getLocalManagerChatUserByNickName(String nickname) {
        for (ChatUser chatUser : chatUsers) {
            if (nickname.equals(chatUser.getNickName())) {
                return chatUser;
            }
        }
        return null;
    }


}
