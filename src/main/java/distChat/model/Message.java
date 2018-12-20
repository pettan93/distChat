package distChat.model;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {

    private String senderName;

    private String content;

    private Date sendTime;

    public Message(String senderName, String content, Date sendTime) {
        this.senderName = senderName;
        this.content = content;
        this.sendTime = sendTime;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
}
