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


public class Server extends PircBot {

    public ChatServiceCallback chatServiceCallback;
    private String server;
    private String name;

    public Server(String server, String name) {
        this.name = name;
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
        Log.d("[IRCd]", "about to connect...");
        this.connect(server);
        Log.d("[IRCd]", "connected");
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
    }

    public void leaveChannel(String channel) {
        this.partChannel(channel);
        //ircCallback.messageCallback("SYSTEM", "Left channel: " + channelItem.getChannelName(), channelItem.getChannelName(), this.getAddress(), ChatItem.PERMISSION_SYSTEM);
    }

    public void connChannel(ChannelItem chan) {
        if (!chan.checkConnected()) {
            Log.d("[IRCd]", "connecting to chan with nick: " + this.name);
            new JoinChannel().execute(chan.getChannelName());
            chan.setConnected(true);
        }
    }

    public String getAddress() {
        return server;
    }

    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        Log.d("[INC]", channel + " " + message);
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
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    private class JoinChannel extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... chan) {
            String channelName = chan[0];
            Log.d("[IRCd]", "about to connect to channel:" + channelName);
            joinChannel(channelName);
            Log.d("[IRCd]", "done connecting to channel");
            return channelName;
        }

        @Override
        protected void onPostExecute(String channelName) {
            //ircCallback.doneJoiningChannel();
            chatServiceCallback.onChannelJoined(new ChannelItem(channelName, server));
        }
    }
}