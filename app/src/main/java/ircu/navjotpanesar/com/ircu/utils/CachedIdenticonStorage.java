package ircu.navjotpanesar.com.ircu.utils;

import android.graphics.Bitmap;

import java.util.HashMap;

/**
 * Created by Navjot on 5/31/2015.
 */
public class CachedIdenticonStorage {
    private IdenticonFactory identiconFactory;
    //maps username (forced lowercase) -> associated bitmap image
    private HashMap<String, Bitmap> identiconMap = new HashMap<String, Bitmap>();
    public CachedIdenticonStorage(IdenticonFactory identiconFactory){
        this.identiconFactory = identiconFactory;
    }

    public Bitmap getImage(String username){
        //identicons are not case sensitive,
        //so we potentially save memory by using lowercase usernames
        username = username.toLowerCase();
        if(identiconMap.containsKey(username)){
            return identiconMap.get(username);
        } else {
            return identiconFactory.getBitmap(username);
        }
    }
}
