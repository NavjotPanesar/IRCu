package ircu.navjotpanesar.com.ircu.activities;

import android.preference.PreferenceActivity;

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