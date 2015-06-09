package ircu.navjotpanesar.com.ircu.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import ircu.navjotpanesar.com.ircu.R;

/**
 * Created by Navjot on 6/9/2015.
 */
public class AddServerDialogFragment extends BaseDialogFragment{

    private EditText serverNameEditView;
    private EditText nickEditView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        serverNameEditView = (EditText) rootView.findViewById(R.id.fragment_add_server_name);
        nickEditView = (EditText) rootView.findViewById(R.id.fragment_add_server_nick);

        getDialog().setTitle("Add a new server");

        rootView.findViewById(R.id.fragment_add_server_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDoneClicked();
            }
        });

        nickEditView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onDoneClicked();
                    return true;
                }
                return false;
            }
        });
        return rootView;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_server_dialog;
    }

    private void onDoneClicked(){
        String server = serverNameEditView.getText().toString();
        String nick = nickEditView.getText().toString();
        if(server.isEmpty()){
            serverNameEditView.setError("Field must not be empty");
            return;
        }

        if(nick.isEmpty()){
            nickEditView.setError("Field must not be empty");
            return;
        }

        Intent returnedIntent = new Intent();
        returnedIntent.putExtra("server", server);
        returnedIntent.putExtra("nick", nick);
        onDialogSuccessListener.onSuccess(returnedIntent);
        dismiss();
    }
}
