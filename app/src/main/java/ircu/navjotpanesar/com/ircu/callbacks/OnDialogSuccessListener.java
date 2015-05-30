package ircu.navjotpanesar.com.ircu.callbacks;

import android.content.Intent;

/**
 * Created by Navjot on 5/30/2015.
 */
//using onActivityResult is kinda ugly, just use this.
public interface OnDialogSuccessListener {
    public void onSuccess(Intent intent);
}
