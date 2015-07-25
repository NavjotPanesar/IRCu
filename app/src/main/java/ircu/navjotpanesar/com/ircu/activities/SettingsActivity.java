package ircu.navjotpanesar.com.ircu.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import ircu.navjotpanesar.com.ircu.R;
import ircu.navjotpanesar.com.ircu.fragments.AddChannelDialogFragment;
import ircu.navjotpanesar.com.ircu.fragments.AddServerDialogFragment;
import ircu.navjotpanesar.com.ircu.fragments.UserSettingsFragment;

/**
 * Created by navjot on 05/06/15.
 */
public class SettingsActivity extends PreferenceActivity
{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
        root.addView(bar, 0); // insert at top
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBuildHeaders(List<Header> target){
        loadHeadersFromResource(R.xml.headers_preference, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName){
        return UserSettingsFragment.class.getName().equals(fragmentName) || AddServerDialogFragment.class.getName().equals(fragmentName);
    }

    @Override
    public void onHeaderClick(Header header, int position) {
        super.onHeaderClick(header, position);
        if (header.id == R.id.settings_add_server) {
            AddServerDialogFragment.newInstance(null).show(getFragmentManager(), "add_new_server");
        }
    }
}