package ircu.navjotpanesar.com.ircu.models;

import ircu.navjotpanesar.com.ircu.pircbot.ChannelItem;

/**
 * Created by Navjot on 6/24/2015.
 */
public abstract class BaseMessage{
    public enum MessageType{
        CHAT,
        SYSTEM
    }

    public abstract MessageType getMessageType();

    protected String message;
    protected ChannelItem channel;

    public BaseMessage(ChannelItem channel, String message){
        this.message = message;
        this.channel = channel;
    }

    public String getMessage() {
        return message;
    }
    public ChannelItem getChannel(){ return channel;}

}
