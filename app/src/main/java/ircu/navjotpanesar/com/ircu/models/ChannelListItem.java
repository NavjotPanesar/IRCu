package ircu.navjotpanesar.com.ircu.models;

import org.pircbotx.Channel;

/**
 * Created by Navjot on 1/1/2015.
 */
public class ChannelListItem {
    private String serverName;
    private String channelName;

    public ChannelListItem(Channel channel){
        this.serverName = channel.getBot().getConfiguration().getServerHostname();
        this.channelName = channel.getName();
    }

    public String getServerName(){
        return serverName;
    }

    public String getChannelName(){
        return channelName;
    }

}
