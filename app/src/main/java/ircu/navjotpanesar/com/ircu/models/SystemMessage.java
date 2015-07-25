package ircu.navjotpanesar.com.ircu.models;

import ircu.navjotpanesar.com.ircu.pircbot.ChannelItem;

/**
 * Created by Navjot on 6/24/2015.
 */
public class SystemMessage extends BaseMessage{

    public enum SystemMessageType{
        INFO,
        TOPIC,
        JOIN,
        LEAVE
    }

    private SystemMessageType systemMessageType;

    public SystemMessage(ChannelItem channel, SystemMessageType systemMessageType, String message) {
        super(channel, message);
        this.systemMessageType = systemMessageType;
    }

    public String getInfoType(){
        switch(systemMessageType){
            case INFO:
                return "Info:";
            case TOPIC:
                return "Topic changed to";
            case JOIN:
                return "";
            case LEAVE:
                return "";
            default:
                return "";
        }
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.SYSTEM;
    }
}
