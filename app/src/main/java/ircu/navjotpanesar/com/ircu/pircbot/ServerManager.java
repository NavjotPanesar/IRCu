package ircu.navjotpanesar.com.ircu.pircbot;

import android.os.AsyncTask;
import android.util.Log;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;

import java.io.IOException;
import java.util.ArrayList;

import ircu.navjotpanesar.com.ircu.callbacks.ChatServiceCallback;

public class ServerManager {
    //TODO: replace this with a set, using the server address as the key.
    ArrayList<Server> serverList;
    ChatServiceCallback chatServiceCallback;
    private String name = "Testeroni232";
    private String password = "";

    public ServerManager(ChatServiceCallback chatServiceCallback) {
        this.chatServiceCallback = chatServiceCallback;
        serverList = new ArrayList<Server>();
    }

    public Server connectToChannel(ChannelItem channelItem) {
        Server server = getServer(channelItem.getServer());
        server.connChannel(channelItem);
        return server;
    }


    //returns a server.
    //this function will create and connect to the server if needed
    private Server getServer(String address) {
        Server server;
        if (!doesServerExist(address)) {
            server = new Server(address, name);
            server.setPassword(password);
            server.chatServiceCallback = chatServiceCallback;
            serverList.add(server);
        } else {
            server = getServerByName(address);
        }
        if (!server.isConnected()) {
            //ircCallback.startLoadingServer();
            new ServerConnectTask().execute(server);
        }
        return server;
    }

    //TODO: replace with set get
    private Server getServerByName(String address) {
        int index = -1;
        for (int i = 0; i < serverList.size(); i++) {
            if (serverList.get(i).getAddress().equals(address)) {
                index = i;
            }
        }
        if (index == -1) {
            Log.d("[SHIT]", "cant find server at that address");
        }
        return serverList.get(index);
    }

    //TODO: replace with set contains method
    private boolean doesServerExist(String address) {
        boolean exists = false;
        for (int i = 0; i < serverList.size(); i++) {
            if (serverList.get(i).getAddress().equals(address)) {
                exists = true;
            }
        }
        return exists;
    }


    private class ServerConnectTask extends AsyncTask<Server, Void, String> {
        @Override
        protected String doInBackground(Server... bot) {
            Log.d("[IRCd]", "starting connection to server");
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

                Log.d("[IRCd]", "done connection to server");
            } else if (result.equals("NickInUse")) {
                //ircCallback.pushMessage("Nickname currently in use, please change it");
            } else {

            }
        }
    }


}