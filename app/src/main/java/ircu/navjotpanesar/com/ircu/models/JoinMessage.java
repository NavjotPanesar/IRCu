package ircu.navjotpanesar.com.ircu.models;

import java.util.Locale;

import ircu.navjotpanesar.com.ircu.pircbot.ChannelItem;

/**
 * Created by Navjot on 7/25/2015.
 */
public class JoinMessage extends SystemMessage {
    public JoinMessage(ChannelItem channel, String author) {
        super(channel, SystemMessageType.JOIN, "");
        this.message = String.format(Locale.US, "%s has joined", author);
    }
}
