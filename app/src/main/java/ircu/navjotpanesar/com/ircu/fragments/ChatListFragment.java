package ircu.navjotpanesar.com.ircu.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;

import ircu.navjotpanesar.com.ircu.R;
import ircu.navjotpanesar.com.ircu.adapters.ChatListAdapter;
import ircu.navjotpanesar.com.ircu.adapters.DividerItemDecoration;
import ircu.navjotpanesar.com.ircu.models.ChatMessage;
import ircu.navjotpanesar.com.ircu.pircbot.ChannelItem;

/**
 * Created by Navjot on 11/27/2014.
 */
public class ChatListFragment extends BaseChatFragment {
    private RecyclerView chatRecyclerView;
    private ChatListAdapter chatListAdapter;
    private ChannelItem currentChannel;

    public ChatListFragment() {
    }

    public static ChatListFragment newInstance() {
        ChatListFragment chatListFragment = new ChatListFragment();
        return chatListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        setupChatListView(rootView);
        return rootView;
    }

    private void setupChatListView(View rootView) {
        chatRecyclerView = (RecyclerView)rootView.findViewById(R.id.fragment_chat_list);
        chatListAdapter = new ChatListAdapter(new ArrayList<ChatMessage>());
        chatRecyclerView.setHasFixedSize(true);
        chatRecyclerView.setAdapter(chatListAdapter);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatRecyclerView.setItemAnimator(new DefaultItemAnimator());
        chatRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public void handleBasicMessage(final ChatMessage message) {
        super.handleBasicMessage(message);
        if(message.getChannel().equals(this.currentChannel)){
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    chatListAdapter.append(message);
                }
            });
        }

    }


    @Override
    public void handleChannelJoin(ChannelItem newChannel) {
        super.handleChannelJoin(newChannel);
        if(this.currentChannel == null){
            this.currentChannel = newChannel;
            switchChannel(newChannel);
        }
    }

    @Override
    public void switchChannel(ChannelItem channel) {
        getActivity().setTitle(currentChannel.getServer() + " / " +currentChannel.getChannelName());
        currentChannel = channel;
        super.switchChannel(channel);
    }

}