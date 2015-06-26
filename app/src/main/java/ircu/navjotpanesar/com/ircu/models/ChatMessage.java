package ircu.navjotpanesar.com.ircu.models;


import android.graphics.Bitmap;

import ircu.navjotpanesar.com.ircu.pircbot.ChannelItem;
import ircu.navjotpanesar.com.ircu.utils.CachedIdenticonStorage;

/**
 * Created by Navjot on 11/28/2014.
 */
public class ChatMessage extends BaseMessage{


    private String author;
    public ChatMessage(ChannelItem channel, String author, String message) {
        super(channel, message);
        this.author = author;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.CHAT;
    }


    public String getAuthor() {
        return author;
    }

    public Bitmap getProfileImage(CachedIdenticonStorage identiconStorage){
        return identiconStorage.getImage(this.getAuthor());
    }
}
