package ircu.navjotpanesar.com.ircu.pircbot;

import android.os.AsyncTask;
import android.util.Log;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

import java.io.IOException;
import java.util.HashMap;

import ircu.navjotpanesar.com.ircu.callbacks.ChatServiceCallback;
import ircu.navjotpanesar.com.ircu.models.ChatMessage;
import ircu.navjotpanesar.com.ircu.models.JoinMessage;
import ircu.navjotpanesar.com.ircu.models.LeaveMessage;
import ircu.navjotpanesar.com.ircu.models.SystemMessage;
import ircu.navjotpanesar.com.ircu.utils.ChatLogger;


public class Server extends PircBot {

    public ChatServiceCallback chatServiceCallback;
    private String server;
    private String username;
    //associates channel name to channel object
    private HashMap<String, ChannelItem> channelMap = new HashMap<String, ChannelItem>();

    public Server(String server, String name) {
        this.username = name;
        this.setName(name);
        this.server = server;
        this.setVerbose(true);
    }

    public void modifyNickSettings(String newNick, String newPass) {
        this.setName(newNick);
        this.setPassword(newPass);
    }

    public String startConnection() throws NickAlreadyInUseException, IOException, IrcException {
        this.setVerbose(false);
        ChatLogger.network("about to connect to " + server  + "...");
        this.connect(server);
        ChatLogger.network("connected");
        return "success";
    }


    public void setPassword(String password) {
        if ((password.isEmpty())) {
            return;
        }
        //call to base class method
        this.identify(password);
    }

    public void quit() {
        for (String chan : getChannels()) {
            this.leaveChannel(chan);
        }
        this.disconnect();
        //ircCallback.pushMessage("Quit server: " + this.server);
    }

    public void send(ChannelItem channel, String message) {
        sendMessage(channel.getChannelName(), message);
        ChatMessage newMessage = new ChatMessage(channel, this.username, message);
        chatServiceCallback.onBasicMessage(newMessage);
    }

    public void leaveChannel(String channel) {
        this.partChannel(channel);
        channelMap.remove(channel);
        //ircCallback.messageCallback("SYSTEM", "Left channel: " + channelItem.getChannelName(), channelItem.getChannelName(), this.getAddress(), ChatItem.PERMISSION_SYSTEM);
    }

    public void connChannel(ChannelItem chan) {
        if (!chan.checkConnected()) {
            ChatLogger.network("connecting to chan with nick: " + this.username);
            new JoinChannel().execute(chan);
        }
    }

    public String getAddress() {
        return server;
    }

    public String getUsername() { return username; }

    public void setUsername(String username){
        this.username = username;
        this.setName(username);
    }

    @Override
    public void onMessage(String channelName, String sender, String login, String hostname, String message) {
        ChatLogger.network("[INC]" + channelName + " " + message);
        //ircCallback.messageCallback(sender, message, channel, this.server, this.getUserPermissionLevel(channel, sender));
        ChannelItem channel = channelMap.get(channelName);
        ChatMessage newMessage = new ChatMessage(channel, sender, message);
        channel.addMessage(newMessage);
        chatServiceCallback.onBasicMessage(newMessage);
    }

    @Override
    public void onPrivateMessage(String sender, String login, String hostname, String message) {
        //ircCallback.pushMessage(sender + "sent: " +message);
    }

    @Override
    protected void onPart(String channelName, String sender, String login, String hostname) {
        ChannelItem channel = channelMap.get(channelName);
        LeaveMessage newLeaveMessage = new LeaveMessage(channel, sender);
        channel.addMessage(newLeaveMessage);
        chatServiceCallback.onSystemMessage(newLeaveMessage);
    }

    /*
    public int getUserPermissionLevel(String channel , String nick){
        if(channel == null || channel == "" || nick == "" || nick == null || getChannelByName(channel).getUserList() == null)
            return ChatItem.PERMISSION_NORMAL;
        int permission = ChatItem.PERMISSION_NORMAL;
        for(User user: getChannelByName(channel).getUserList()){
            if(user.equals(nick)){
                if(user.isOp()){
                    permission = ChatItem.PERMISSION_OP;
                }
            }
        }
        return permission;
    }*/

    @Override
    protected void onJoin(String channelName, String sender, String login, String hostname) {
        ChannelItem channel = channelMap.get(channelName);
        JoinMessage newJoinMessage = new JoinMessage(channel, sender);
        channel.addMessage(newJoinMessage);
        chatServiceCallback.onSystemMessage(newJoinMessage);
    }

    @Override
    public void onTopic(String channelName, String topic, String setBy, long date, boolean changed) {
        //ircCallback.messageCallback("SYSTEM", "Topic: " + topic, channel, this.getAddress(), ChatItem.PERMISSION_SYSTEM);
        // getChannelByName(channel).setTopic(topic);
        ChannelItem channel = channelMap.get(channelName);
        SystemMessage newTopicMessage = new SystemMessage(channel, SystemMessage.SystemMessageType.TOPIC, topic );
        channel.addMessage(newTopicMessage);
        chatServiceCallback.onSystemMessage(newTopicMessage);
    }

    @Override
    public void onUserList(String channel, User[] users) {
        //ircCallback.messageCallback("SYSTEM", "Users in room: " + userString, channel, this.getAddress(), ChatItem.PERMISSION_SYSTEM);
    }

    @Override
    public int hashCode() {
        return this.server.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Server other = (Server) obj;
        if (this.server != other.server)
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }

    private class JoinChannel extends AsyncTask<ChannelItem, Void, ChannelItem> {
        @Override
        protected ChannelItem doInBackground(ChannelItem... chan) {
            ChannelItem channel = chan[0];
            ChatLogger.network("kicking off asynctask for " + channel.getChannelName());
            joinChannel(channel.getChannelName());
            ChatLogger.network("connected to " + channel.getChannelName());
            return channel;
        }

        @Override
        protected void onPostExecute(ChannelItem channel) {
            //ircCallback.doneJoiningChannel();
            channel.setConnected(true);
            channelMap.put(channel.getChannelName(), channel);
            chatServiceCallback.onChannelJoined(channel);
        }
    }
}