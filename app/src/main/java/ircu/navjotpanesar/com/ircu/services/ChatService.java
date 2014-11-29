package ircu.navjotpanesar.com.ircu.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.io.IOException;

import ircu.navjotpanesar.com.ircu.callbacks.ChatServiceCallback;
import ircu.navjotpanesar.com.ircu.models.ChatMessage;
import ircu.navjotpanesar.com.ircu.utils.ChatLogger;

/**
 * Created by Navjot on 11/27/2014.
 */
public class ChatService extends Service {
    private PircBotX server;
    private final IBinder binder = new LocalBinder();
    private ChatServiceCallback chatServiceCallback;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Configuration configuration = new Configuration.Builder()
                .setName("Testerin195") //Set the nick of the bot. CHANGE IN YOUR CODE
                .setServerHostname("irc.freenode.net") //Join the freenode network
                .addAutoJoinChannel("#pircbot") //Join the official #pircbotx channel
                .addListener(new ChatListener()) //Add our listener that will be called on Events
                .buildConfiguration();

        //Create our bot with the configuration
        server = new PircBotX(configuration);
        //Connect to the server
        (new ChatTask()).execute(server);
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

    public void registerCallback(ChatServiceCallback chatServiceCallback) {
        this.chatServiceCallback = chatServiceCallback;
    }

    private class ChatTask extends AsyncTask<PircBotX, Void, Void> {
        @Override
        protected Void doInBackground(PircBotX... params) {
            try {
                params[0].startBot();
            } catch (IrcException exception) {
                ChatLogger.v("IRC Exception: " + exception.getMessage());
            } catch (IOException exception) {
                ChatLogger.v("IOException: " + exception.getMessage());
            }
            return null;
        }
    }

    private class ChatListener extends ListenerAdapter {
        public ChatListener() {
        }

        @Override
        public void onGenericMessage(GenericMessageEvent event) {
            ChatLogger.v("MESSAGE: " + event.getMessage());
            //TODO: Fire loader into database then have loader run the callback to the bound activity
            if(chatServiceCallback != null){
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setMessage(event.getMessage());
                chatMessage.setAuthor(event.getUser().getNick());
                chatServiceCallback.onBasicMessage(chatMessage);
            }
        }
    }
}
