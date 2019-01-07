package distChat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageLog {

    private String ownerNickname;

    private List<String> content = new ArrayList<>();

    public MessageLog(String ownerNickname) {
        this.ownerNickname = ownerNickname;
        this.log("Log created");
    }

    public void log(String msg){
        var sb = new StringBuilder();
        sb.append(Utils.formatDate(new Date(), "dd.MM.yyyy HH:mm:ss.SSS")).append(" | ");
        sb.append(ownerNickname).append(" | ");
        sb.append(msg);
        content.add(sb.toString());


        System.out.println(sb.toString());


    }

    public String getContent() {

        StringBuilder sb = new StringBuilder();
        for (String s : content) {
            sb.append("<p>");
            sb.append(s);
            sb.append("</p>");
        }


        return sb.toString();
    }


}
