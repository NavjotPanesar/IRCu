package ircu.navjotpanesar.com.ircu.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;

import org.pircbotx.Channel;
import org.pircbotx.Configuration;
import org.pircbotx.MultiBotManager;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.ServerResponseEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ircu.navjotpanesar.com.ircu.callbacks.ChatServiceCallback;
import ircu.navjotpanesar.com.ircu.models.ChatMessage;
import ircu.navjotpanesar.com.ircu.utils.ChatLogger;

/**
 * Created by Navjot on 11/27/2014.
 */
public class ChatService extends Service {
    private MultiBotManager<PircBotX> manager;
    private final IBinder binder = new LocalBinder();
    private List<ChatServiceCallback> observers = new ArrayList<ChatServiceCallback>();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        manager = new MultiBotManager();

        Configuration configuration1 = new Configuration.Builder()
                .setName("Testerin195") //Set the nick of the bot. CHANGE IN YOUR CODE
                .setAutoNickChange(true)
                .setServerHostname("irc.freenode.net") //Join the freenode network
                .addAutoJoinChannel("#pircbot") //Join the official #pircbotx channel
                .addListener(new ChatListener()) //Add our listener that will be called on Events
                .buildConfiguration();

        Configuration configuration2 = new Configuration.Builder()
                .setName("Testerin195") //Set the nick of the bot. CHANGE IN YOUR CODE
                .setAutoNickChange(true)
                .setServerHostname("irc.rizon.net") //Join the freenode network
                .addListener(new ChatListener()) //Add our listener that will be called on Events
                .buildConfiguration();


        manager.addBot(configuration1);
        manager.addBot(configuration2);

        //Connect to the server
        (new ChatTask()).execute(manager);
        return Service.START_NOT_STICKY;
    }

    public class LocalBinder extends Binder {
        public ChatService getService() {
            return ChatService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void registerObserver(ChatServiceCallback observer) {
        observers.add(observer);
    }

    public void notifyObserversBasicMessage(ChatMessage message) {
        for (final ChatServiceCallback observer : observers) {
            observer.onBasicMessage(message);
        }
    }

    public void notifyObserversChannelJoined(Channel channel) {
        for (final ChatServiceCallback observer : observers) {
            observer.onChannelJoined(channel);
        }
    }


    private class ChatTask extends AsyncTask<MultiBotManager<PircBotX>, Void, Void> {
        @Override
        protected Void doInBackground(MultiBotManager<PircBotX>... params) {
            ChatLogger.v("MANAGER: " + params[0].getBots());
            try {
                params[0].start();
            } catch (Exception exception) {
                ChatLogger.v("Manager Exception: " + exception.getMessage());
            }
            return null;
        }
    }

    private class ChatListener extends ListenerAdapter {
        public ChatListener() {
        }

        @Override
        public void onJoin(JoinEvent event) throws Exception {
            super.onJoin(event);
            String joinedUser = event.getUser().getNick();
            String currentUser = event.getBot().getNick();
            if(joinedUser.equals(currentUser)){
                ChatLogger.v("Callback join channel");
                Channel joinedChannel = event.getChannel();
                notifyObserversChannelJoined(joinedChannel);
            }
        }

        @Override
        public void onMessage(MessageEvent event) throws Exception {
            super.onMessage(event);
            ChatLogger.v("MESSAGE: " + event.getMessage());
            //TODO: Fire loader into database then have loader run the callback to the bound activity
                Channel channel = event.getChannel();
                String author = event.getUser().getNick();
                String message = event.getMessage();
                ChatMessage chatMessage = new ChatMessage(channel, author, message);
                notifyObserversBasicMessage(chatMessage);
        }

//        @Override
//        public void onGenericMessage(GenericMessageEvent event) {

//        }

        @Override
        public void onConnect(ConnectEvent event) throws Exception {
            super.onConnect(event);
        }

        @Override
        public void onServerResponse(ServerResponseEvent event) throws Exception {
            super.onServerResponse(event);
            ChatLogger.v("SERVER: " + event.getRawLine());
        }
    }
}
