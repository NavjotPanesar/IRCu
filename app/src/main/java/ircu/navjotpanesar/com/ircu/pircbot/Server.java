package ircu.navjotpanesar.com.ircu.pircbot;

import android.os.AsyncTask;
import android.util.Log;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

import java.io.IOException;

import ircu.navjotpanesar.com.ircu.callbacks.ChatServiceCallback;
import ircu.navjotpanesar.com.ircu.models.ChatMessage;
import ircu.navjotpanesar.com.ircu.utils.ChatLogger;


public class Server extends PircBot {

    public ChatServiceCallback chatServiceCallback;
    private String server;
    private String username;

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

    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        ChatLogger.network("[INC]" + channel + " " + message);
        //ircCallback.messageCallback(sender, message, channel, this.server, this.getUserPermissionLevel(channel, sender));
        chatServiceCallback.onBasicMessage(new ChatMessage(new ChannelItem(channel, server), sender, message));
    }

    @Override
    public void onPrivateMessage(String sender, String login, String hostname, String message) {
        //ircCallback.pushMessage(sender + "sent: " +message);
    }

    @Override
    protected void onPart(String channel, String sender, String login, String hostname) {
        //ircCallback.messageCallback("SYSTEM", sender + " just left", channel, this.getAddress(), ChatItem.PERMISSION_SYSTEM);
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
    protected void onJoin(String channel, String sender, String login, String hostname) {
        //ircCallback.messageCallback("SYSTEM", sender + " has joined", channel, this.getAddress(), ChatItem.PERMISSION_SYSTEM);

    }

    @Override
    public void onTopic(String channel, String topic, String setBy, long date, boolean changed) {
        //ircCallback.messageCallback("SYSTEM", "Topic: " + topic, channel, this.getAddress(), ChatItem.PERMISSION_SYSTEM);
        // getChannelByName(channel).setTopic(topic);
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
            chatServiceCallback.onChannelJoined(channel);
        }
    }
}