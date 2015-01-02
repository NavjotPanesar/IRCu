package ircu.navjotpanesar.com.ircu.models;

import org.pircbotx.Channel;

/**
 * Created by Navjot on 11/28/2014.
 */
public class ChatMessage {
    private String author;
    private String message;
    private Channel channel;

    public ChatMessage(Channel channel, String author, String message){
        this.author = author;
        this.message = message;
        this.channel = channel;
    }

    public String getAuthor() {
        return author;
    }
    public String getMessage() {
        return message;
    }
    public Channel getChannel(){ return channel;}

}
