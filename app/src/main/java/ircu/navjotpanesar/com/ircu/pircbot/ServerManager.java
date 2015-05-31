package ircu.navjotpanesar.com.ircu.pircbot;

import android.os.AsyncTask;
import android.util.Log;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import ircu.navjotpanesar.com.ircu.callbacks.ChatServiceCallback;
import ircu.navjotpanesar.com.ircu.utils.ChatLogger;

public class ServerManager {
    ChatServiceCallback chatServiceCallback;
    private String name = "Testeroni232";
    private String password = "";

    //Maps server URL to the associated server object
    private HashMap<String, Server> serverMap = new HashMap<String,Server>();

    public ServerManager(ChatServiceCallback chatServiceCallback) {
        this.chatServiceCallback = chatServiceCallback;
    }

    public void sendMessage(ChannelItem channelItem, String message){
        Server server = getServer(channelItem.getServer());
        server.send(channelItem, message);
    }

    public Server connectToChannel(ChannelItem channelItem) {
        ChatLogger.network("Starting channel join for " + channelItem.getChannelName());
        Server server = getServer(channelItem.getServer());
        ChatLogger.network("Obtained server " + server.getAddress());
        server.connChannel(channelItem);
        return server;
    }


    //returns a server.
    //this function will create and connect to the server if needed
    private Server getServer(String address) {
        Server server;
        if (!serverMap.containsKey(address)) {
            server = new Server(address, name);
            server.setPassword(password);
            server.chatServiceCallback = chatServiceCallback;
            serverMap.put(address ,server);
        } else {
            server = serverMap.get(address);
        }
        if (!server.isConnected()) {
            //ircCallback.startLoadingServer();
            new ServerConnectTask().execute(server);
        }
        return server;
    }



    private class ServerConnectTask extends AsyncTask<Server, Void, String> {
        @Override
        protected String doInBackground(Server... bot) {
            ChatLogger.network("Starting connection to " + bot[0].getAddress());
            String result;
            try {
                result = bot[0].startConnection();
            } catch (NickAlreadyInUseException e) {
                result = "NickInUse";
                e.printStackTrace();
            } catch (IOException e) {
                result = e.getMessage();
                e.printStackTrace();
            } catch (IrcException e) {
                result = e.getMessage();
                e.printStackTrace();
            }
            return result;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if (result.equals("success")) {
                //ircCallback.doneLoadingServer();
                ChatLogger.network("finished connection");
            } else if (result.equals("NickInUse")) {
                //ircCallback.pushMessage("Nickname currently in use, please change it");
            } else {

            }
        }
    }


}