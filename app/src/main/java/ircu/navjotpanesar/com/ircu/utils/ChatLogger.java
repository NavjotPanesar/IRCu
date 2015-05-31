package ircu.navjotpanesar.com.ircu.utils;

import android.util.Log;

/**
 * Created by Navjot on 11/24/2014.
 */
public class ChatLogger {
    public static void v(String message){
        Log.d("CHATLOGGER", "CHATLOGGER " + message);
    }
    public static void network(String message) {
        Log.d("IRCNETWORK", message);
    }
}
