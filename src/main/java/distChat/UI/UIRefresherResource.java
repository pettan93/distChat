package distChat.UI;

import distChat.model.ChatUser;
import io.javalin.Javalin;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class UIRefresherResource {

    private Javalin app;

    private ChatUser chatUser;

    public UIRefresherResource(Javalin userApp, ChatUser chatUser) {
        this.app = userApp;
        this.chatUser = chatUser;
        Properties p = new Properties();
        p.setProperty("resource.loader", "class");
        p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(p);
    }

    public void buildResource() {

        // LOG
        app.get("/refresher/log", ctx -> {
            VelocityContext context = new VelocityContext();
            context.put("chatUser", this.chatUser);
            StringWriter w = new StringWriter();
            Velocity.mergeTemplate("public/refresher/log.vm", context, w);
            ctx.result(w.toString());
        });


        // CHAT ROOMS MENU
        app.get("/refresher/chatroommenu", ctx -> {
            VelocityContext context = new VelocityContext();
            context.put("chatUser", this.chatUser);
            StringWriter w = new StringWriter();
            Velocity.mergeTemplate("public/refresher/chatroomsMenu.vm", context, w);
            ctx.result(w.toString());
        });

        // CHAT ROOM DETAAIL
        app.get("/refresher/chatroom/:chatroomname", ctx -> {
            VelocityContext context = new VelocityContext();
            context.put("chatUser", this.chatUser);
            context.put("chatRoomName", ctx.pathParam("chatroomname"));
            StringWriter w = new StringWriter();
            Velocity.mergeTemplate("public/refresher/chatroom.vm", context, w);
            ctx.result(w.toString());
        });


    }


}
