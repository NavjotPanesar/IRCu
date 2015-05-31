package ircu.navjotpanesar.com.ircu.pircbot;

public class ChatItem {
    public static final int PERMISSION_NORMAL = 0;
    public static final int PERMISSION_SYSTEM = 1;
    public static final int PERMISSION_OP = 2;

    private String from;
    private String body;
    private String channel;
    private int permissionLevel;

    public ChatItem(String from, String body, String channel) {
        this.from = from;
        this.body = body;
        this.channel = channel;
        this.permissionLevel = PERMISSION_NORMAL;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(int permissionLevel) {
        this.permissionLevel = permissionLevel;
    }


}