package ircu.navjotpanesar.com.ircu.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import ircu.navjotpanesar.com.ircu.R;

/**
 * Created by navjot on 05/06/15.
 */
public class SettingsFragment extends PreferenceFragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_preference);
    }
}