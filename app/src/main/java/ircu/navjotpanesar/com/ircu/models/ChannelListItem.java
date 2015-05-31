package ircu.navjotpanesar.com.ircu.models;


/**
 * Created by Navjot on 1/1/2015.
 */
public class ChannelListItem {
    private String serverName;
    private String channelName;

    public String getServerName(){
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelName(){
        return channelName;
    }

    public ChannelListItem(String channelName, String serverName){
        this.serverName = serverName;
        this.channelName = channelName;
    }
}
