package ircu.navjotpanesar.com.ircu.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import ircu.navjotpanesar.com.ircu.fragments.ChatListFragment;
import ircu.navjotpanesar.com.ircu.R;
import ircu.navjotpanesar.com.ircu.services.ChatService;
import ircu.navjotpanesar.com.ircu.utils.ChatLogger;


public class ChatActivity extends BaseActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_chat);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, ChatListFragment.newInstance())
                    .commit();
        }
        super.onCreate(savedInstanceState);
    }

}
