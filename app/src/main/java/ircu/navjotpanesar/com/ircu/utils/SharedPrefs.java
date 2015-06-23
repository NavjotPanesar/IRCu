package ircu.navjotpanesar.com.ircu.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Navjot on 6/23/2015.
 */
public class SharedPrefs {
    public static String getDefaultUsername(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("default-nickname", "");
    }
}
