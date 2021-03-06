package ircu.navjotpanesar.com.ircu.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ircu.navjotpanesar.com.ircu.callbacks.ChatServiceCallback;
import ircu.navjotpanesar.com.ircu.models.ChatMessage;
import ircu.navjotpanesar.com.ircu.models.SystemMessage;
import ircu.navjotpanesar.com.ircu.pircbot.ChannelItem;
import ircu.navjotpanesar.com.ircu.pircbot.ServerManager;
import ircu.navjotpanesar.com.ircu.utils.ChatLogger;
import ircu.navjotpanesar.com.ircu.utils.SharedPrefs;

/**
 * Created by Navjot on 11/27/2014.
 */
public class ChatService extends Service {
    private final IBinder binder = new LocalBinder();
    private List<ChatServiceCallback> observers = new ArrayList<ChatServiceCallback>();
    private ServerManager serverManager;

    @Override
    public void onCreate() {
        super.onCreate();

        serverManager = new ServerManager(this ,serverManagerCallback);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {
        public ChatService getService() {
            return ChatService.this;
        }
    }

    public ChatService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        //serverManager.connectToChannel(new ChannelItem("#pircbot", "irc.freenode.net"));
        return Service.START_NOT_STICKY;
    }

    public void registerObserver(ChatServiceCallback observer) {
        observers.add(observer);
    }

    public void notifyObserversBasicMessage(ChatMessage message) {
        for (final ChatServiceCallback observer : observers) {
            observer.onBasicMessage(message);
        }
    }

    public void notifyObserversChannelJoined(ChannelItem channel) {
        for (final ChatServiceCallback observer : observers) {
            observer.onChannelJoined(channel);
        }
    }

    public void notifyObserversSystemMessage(SystemMessage message) {
        for (final ChatServiceCallback observer : observers) {
            observer.onSystemMessage(message);
        }
    }

    private ChatServiceCallback serverManagerCallback = new ChatServiceCallback() {

        // these observer functions just propagate the exact same callback to all observers

        @Override
        public void onBasicMessage(ChatMessage message) {
            notifyObserversBasicMessage(message);
        }

        @Override
        public void onSystemMessage(SystemMessage message) {
            notifyObserversSystemMessage(message);
        }

        @Override
        public void onChannelJoined(ChannelItem channel) {
            notifyObserversChannelJoined(channel);
        }


    };

    public void joinChannel(ChannelItem channelItem){
        serverManager.connectToChannel(channelItem);
    }

    public void sendMessage(ChannelItem channelItem, String message) {
        serverManager.sendMessage(channelItem, message);
    }
}
