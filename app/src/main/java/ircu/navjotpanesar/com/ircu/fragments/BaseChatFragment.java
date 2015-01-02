package ircu.navjotpanesar.com.ircu.fragments;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.pircbotx.Channel;

import ircu.navjotpanesar.com.ircu.activities.ChatActivity;
import ircu.navjotpanesar.com.ircu.callbacks.ChatServiceCallback;
import ircu.navjotpanesar.com.ircu.models.ChatMessage;
import ircu.navjotpanesar.com.ircu.services.ChatService;
import ircu.navjotpanesar.com.ircu.utils.ChatLogger;

/**
 * Created by Navjot on 11/24/2014.
 */
public abstract class BaseChatFragment extends Fragment {


    private ChatService chatService;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // use this to start and trigger a service
//        Intent i= new Intent(getActivity(), ChatService.class);
//        // potentially add data to the intent
//        i.putExtra("KEY1", "Value to be used by the service");
//        getActivity().startService(i);


        startChatService();
        bindChatService();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void handleBasicMessage(ChatMessage message){};

    public void handleChannelJoin(Channel channel){};

    private ChatServiceCallback getChatServiceCallback(){
        return new ChatServiceCallback(){
            @Override
            public void onBasicMessage(ChatMessage message) {
                handleBasicMessage(message);
            }

            @Override
            public void onChannelJoined(Channel channel) {
                handleChannelJoin(channel);
            }

        };
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            chatService = ((ChatService.LocalBinder) service).getService();
            chatService.registerObserver(getChatServiceCallback());
            ChatLogger.v("Registered callback");
        }

        public void onServiceDisconnected(ComponentName className) {
            chatService = null;
        }
    };

    private void startChatService() {
        getActivity().startService(new Intent(getActivity(),
                ChatService.class));
    }


    private void bindChatService() {
        getActivity().bindService(new Intent(getActivity(),
                ChatService.class), mConnection, Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
    }

    private void unbindChatService() {
        getActivity().unbindService(mConnection);
    }

    private void stopChatService() {
        getActivity().stopService(new Intent(getActivity(),
                ChatService.class));
    }



}
