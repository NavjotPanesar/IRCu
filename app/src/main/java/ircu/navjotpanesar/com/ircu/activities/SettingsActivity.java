package ircu.navjotpanesar.com.ircu.activities;

import android.preference.PreferenceActivity;

import java.util.List;

import ircu.navjotpanesar.com.ircu.R;
import ircu.navjotpanesar.com.ircu.fragments.SettingsFragment;

/**
 * Created by navjot on 05/06/15.
 */
public class SettingsActivity extends PreferenceActivity
{
    @Override
    public void onBuildHeaders(List<Header> target)
    {
        loadHeadersFromResource(R.xml.headers_preference, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName)
    {
        return SettingsFragment.class.getName().equals(fragmentName);
    }
}