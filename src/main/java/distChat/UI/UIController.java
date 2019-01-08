package distChat.UI;

import distChat.model.ChatUser;
import io.javalin.Javalin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UIController {

    public static ArrayList<ChatUser> uiUsers = new ArrayList();


    public static void initManager() {

        Javalin app = Javalin.create()
                .enableStaticFiles("/public")
                .enableRouteOverview("/path")
                .start(5000);

        app.get("/", ctx -> {
            Map<String, Object> model = new HashMap<>();
            model.put("uiUsers", uiUsers);
            ctx.render("public/manager.vm", model);
        });
    }

    public static void buildUserController(List<ChatUser> chatUserList) {
        for (ChatUser chatUser : chatUserList) {
            buildUserController(chatUser);
        }
    }

    public static void buildUserController(ChatUser chatUser) {

        int uiport = chatUser.getKadNode().getPort() + 1000;

        Javalin app = Javalin.create()
                .enableStaticFiles("/public")
                .enableRouteOverview("/path")
                .start(uiport);

        app.get("/", ctx -> {
            Map<String, Object> model = new HashMap<>();
            model.put("chatUser", chatUser);
            ctx.render("public/interface-main.vm", model);
        });

        app.get("/dht", ctx -> {
            Map<String, Object> model = new HashMap<>();
            model.put("chatUser", chatUser);
            ctx.render("public/interface-dht.vm", model);
        });


        app.get("/routing", ctx -> {
            Map<String, Object> model = new HashMap<>();
            model.put("chatUser", chatUser);
            ctx.render("public/interface-routing.vm", model);
        });

        app.get("/finder", ctx -> {
            Map<String, Object> model = new HashMap<>();
            if (ctx.queryParam("chatRoomName") != null) {
                System.out.println("Hledame");
                model.put("searchQuery", ctx.queryParam("chatRoomName"));
                model.put("result", ActionProcessor.processFindChatroom(chatUser, ctx.queryParam("chatRoomName")));
            }
            model.put("chatUser", chatUser);
            ctx.render("public/interface-finder.vm", model);
        });

        app.get("/joinchatroom", ctx -> {
            Map<String, Object> model = new HashMap<>();
            if (ctx.queryParam("chatRoomName") != null) {
                ActionProcessor.processJoinChatroom(chatUser, ctx.queryParam("chatRoomName"));
            }
            model.put("chatUser", chatUser);
            ctx.redirect("/chatroom/" + ctx.queryParam("chatroomname"));
        });


        app.get("/chatroom/:chatroomname", ctx -> {
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


        uiUsers.add(chatUser);

        System.out.println("Created UI for chatuser [" + chatUser.getNickName() + "] on port [" + uiport + "]");
    }


}
