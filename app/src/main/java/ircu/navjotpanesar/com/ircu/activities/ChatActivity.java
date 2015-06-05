package ircu.navjotpanesar.com.ircu.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import ircu.navjotpanesar.com.ircu.fragments.ChannelListFragment;
import ircu.navjotpanesar.com.ircu.fragments.ChatListFragment;
import ircu.navjotpanesar.com.ircu.R;
import ircu.navjotpanesar.com.ircu.pircbot.ChannelItem;
import ircu.navjotpanesar.com.ircu.services.ChatService;
import ircu.navjotpanesar.com.ircu.utils.ChatLogger;


public class ChatActivity extends BaseActionBarActivity implements ChannelListFragment.OnChannelSwitchListener{

    private ChatListFragment chatListFragment;
    private ChannelListFragment channelListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            chatListFragment = ChatListFragment.newInstance() ;
            getFragmentManager().beginTransaction()
                    .add(R.id.container, chatListFragment)
                    .commit();

            channelListFragment = ChannelListFragment.newInstance();
            getFragmentManager().beginTransaction()
                    .add(R.id.left_drawer_container, channelListFragment)
                    .commit();
        }
    }

    @Override
    int getContentViewId() {
        return R.layout.activity_chat;
    }


    private void startChatService() {
        startService(new Intent(this, ChatService.class));
    }

    @Override
    public void channelSwitch(ChannelItem channel) {
        setTitle(channel.getServer() + " / " + channel.getChannelName());
        chatListFragment.switchChannel(channel);
        getDrawerLayout().closeDrawers();
    }

}
