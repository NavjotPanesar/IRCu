package ircu.navjotpanesar.com.ircu.fragments;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import ircu.navjotpanesar.com.ircu.R;
import ircu.navjotpanesar.com.ircu.callbacks.OnDialogSuccessListener;

/**
 * Created by Navjot on 5/30/2015.
 */
public class AddChannelDialogFragment extends BaseDialogFragment{


    private Spinner serverSpinner;
    private EditText channelNameEditView;

    public static AddChannelDialogFragment newInstance(OnDialogSuccessListener onDialogSuccessListener) {
        AddChannelDialogFragment fragment = new AddChannelDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setOnDialogSuccessListener(onDialogSuccessListener);
        return fragment;
    }

    public AddChannelDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater,container,savedInstanceState);
        serverSpinner = (Spinner) rootView.findViewById(R.id.fragment_add_channel_server);
        channelNameEditView = (EditText) rootView.findViewById(R.id.fragment_add_channel_name);

        //TODO: should grab servers from a pre-populated list that can be added to by the user
        ArrayList<String> servers = new ArrayList<String>();
        servers.add("irc.freenode.net");
        servers.add("irc.rizon.net");
        ArrayAdapter<String> serverSpinnerAdapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, servers);

        serverSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serverSpinner.setAdapter(serverSpinnerAdapter);
        getDialog().setTitle("Add a new channel");

        rootView.findViewById(R.id.fragment_add_channel_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDoneClicked();
            }
        });

        channelNameEditView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
        return R.layout.fragment_add_channel_dialog;
    }

    private void onDoneClicked(){
        String server = serverSpinner.getSelectedItem().toString();
        String channel = channelNameEditView.getText().toString();
        if(channel.isEmpty()){
            channelNameEditView.setError("Field must not be empty");
            return;
        }

        if(server.isEmpty()){
            return;
        }

        Intent returnedIntent = new Intent();
        returnedIntent.putExtra("server", server);
        returnedIntent.putExtra("channel", channel);
        onDialogSuccessListener.onSuccess(returnedIntent);
        dismiss();
    }

}
