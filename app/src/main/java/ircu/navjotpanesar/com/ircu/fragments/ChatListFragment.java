package ircu.navjotpanesar.com.ircu.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


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
    private EditText messageEditView;

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
        messageEditView = (EditText) rootView.findViewById(R.id.fragment_chat_message);
        ImageButton sendButton = (ImageButton) rootView.findViewById(R.id.fragment_chat_send);
        setupChatListView(rootView);
        sendButton.setOnClickListener(messageClickListener);
        messageEditView.setOnEditorActionListener(messageImeListener);
        return rootView;
    }

    private View.OnClickListener messageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sendMessage();
        }
    };

    private TextView.OnEditorActionListener messageImeListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if(actionId == EditorInfo.IME_ACTION_SEND){
                sendMessage();
                return true;
            }
            return false;
        }
    };

    private void sendMessage(){
        hideKeyboard();
        String message = messageEditView.getText().toString();
        super.sendMessage(currentChannel, message);
        messageEditView.setText("");
    }

    private void hideKeyboard() {
        View v = getActivity().getWindow().getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    private void setupChatListView(View rootView) {
        chatRecyclerView = (RecyclerView)rootView.findViewById(R.id.fragment_chat_list);
        chatListAdapter = new ChatListAdapter(new ArrayList<ChatMessage>(), getActivity());
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
        currentChannel = channel;
        super.switchChannel(channel);
    }

}
