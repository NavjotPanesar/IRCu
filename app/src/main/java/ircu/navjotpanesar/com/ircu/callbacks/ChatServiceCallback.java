package ircu.navjotpanesar.com.ircu.callbacks;

import ircu.navjotpanesar.com.ircu.models.ChatMessage;

/**
 * Created by Navjot on 11/28/2014.
 */
public interface ChatServiceCallback {
    public void onBasicMessage(ChatMessage message);
}
