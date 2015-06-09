package ircu.navjotpanesar.com.ircu.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import ircu.navjotpanesar.com.ircu.R;
import ircu.navjotpanesar.com.ircu.callbacks.OnDialogSuccessListener;

/**
 * Created by Navjot on 6/9/2015.
 */
public abstract class BaseDialogFragment extends DialogFragment {

    protected OnDialogSuccessListener onDialogSuccessListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container);
        setDialogSize();
        return view;
    }

    public void setOnDialogSuccessListener(OnDialogSuccessListener onDialogSuccessListener){
        this.onDialogSuccessListener = onDialogSuccessListener;
    }

    protected void setDialogSize() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        getDialog().getWindow().setLayout((6 * width)/7, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    protected abstract int getLayoutId();
}
