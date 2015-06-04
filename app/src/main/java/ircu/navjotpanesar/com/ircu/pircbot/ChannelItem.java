package ircu.navjotpanesar.com.ircu.pircbot;

import android.util.Log;

import org.jibble.pircbot.User;

import java.util.ArrayList;
import java.util.Arrays;

import ircu.navjotpanesar.com.ircu.models.ChatMessage;

public class ChannelItem {
    private String channel;
    private String server;
    private boolean connected = false;
    private ArrayList<User> userList;
    private ArrayList<ChatMessage> chatList = new ArrayList<ChatMessage>();
    private String topic;

    public ChannelItem(String channel, String server) {
        this.channel = channel;
        this.server = server;
    }

    public boolean checkConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void addMessage(ChatMessage chatItem){
        chatList.add(chatItem);
    }

    public ArrayList<ChatMessage> getChatList(){
        return chatList;
    }

    public void removeUser(User user) {
        userList.remove(user);
    }


    public void addUser(User user) {
        userList.add(user);
    }

    public void removeUser(String username) {
        for (User userObject : userList) {
            if (userObject.getNick().equals(username)) {
                userList.remove(userObject);
            }
        }
    }

    public void addUser(String username) {
        Log.d("USER", username);
        for (User userObject : userList) {
            if (userObject.getNick().equals(username)) {
                userList.add(userObject);
            }
            Log.d("USERs", userObject.getNick());
        }
    }


    public String getChannelName() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

	/*public User[] getUserList() {
        return userList;
	}*/

    public void setUserListFromArray(User[] userList) {
        this.userList = new ArrayList<User>(Arrays.asList(userList));
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public ArrayList<User> getUserList() {
        // TODO Auto-generated method stub
        return userList;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ChannelItem))
            return false;
        if (obj == this)
            return true;

        ChannelItem rhs = (ChannelItem) obj;
        return (channel.equals(rhs.channel) && server.equals(rhs.server));
    }


}