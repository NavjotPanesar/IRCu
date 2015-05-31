package ircu.navjotpanesar.com.ircu.models;


import ircu.navjotpanesar.com.ircu.pircbot.ChannelItem;

/**
 * Created by Navjot on 11/28/2014.
 */
public class ChatMessage {
    private String author;
    private String message;
    private ChannelItem channel;

    public ChatMessage(ChannelItem channel, String author, String message){
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
    public ChannelItem getChannel(){ return channel;}

}
