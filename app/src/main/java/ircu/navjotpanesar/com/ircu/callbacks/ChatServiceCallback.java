package ircu.navjotpanesar.com.ircu.callbacks;

import ircu.navjotpanesar.com.ircu.models.ChatMessage;
import ircu.navjotpanesar.com.ircu.models.SystemMessage;
import ircu.navjotpanesar.com.ircu.pircbot.ChannelItem;

/**
 * Created by Navjot on 11/28/2014.
 */
public interface ChatServiceCallback {
    public void onBasicMessage(ChatMessage message);
    public void onSystemMessage(SystemMessage message);
    public void onChannelJoined(ChannelItem channel);
}
