package ircu.navjotpanesar.com.ircu.fragments;

import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
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
import java.util.List;

import ircu.navjotpanesar.com.ircu.R;
import ircu.navjotpanesar.com.ircu.callbacks.OnDialogSuccessListener;
import ircu.navjotpanesar.com.ircu.contentproviders.ChannelsContentProvider;
import ircu.navjotpanesar.com.ircu.contentproviders.ServersContentProvider;
import ircu.navjotpanesar.com.ircu.database.ChannelsTable;
import ircu.navjotpanesar.com.ircu.database.ServersTable;
import ircu.navjotpanesar.com.ircu.pircbot.ChannelItem;

/**
 * Created by Navjot on 5/30/2015.
 */
public class AddChannelDialogFragment extends BaseDialogFragment{


    private Spinner serverSpinner;
    private EditText channelNameEditView;
    private ArrayAdapter<String> serverSpinnerAdapter;

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

        loadData();

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

        ContentValues values = new ContentValues();
        values.put(ChannelsTable.COLUMNS.CHANNEL, channel);
        values.put(ChannelsTable.COLUMNS.SERVER, server);
        getActivity().getContentResolver().insert(ChannelsContentProvider.CONTENT_URI, values);

        Intent returnedIntent = new Intent();
        returnedIntent.putExtra("server", server);
        returnedIntent.putExtra("channel", channel);
        onDialogSuccessListener.onSuccess(returnedIntent);
        dismiss();
    }

    private void loadData() {
        getLoaderManager().initLoader(0, null, getContentLoaderCallback());
    }

    private LoaderManager.LoaderCallbacks<Cursor> getContentLoaderCallback(){
        return new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                String[] projection = { ServersTable.COLUMNS.ID, ServersTable.COLUMNS.SERVER, ServersTable.COLUMNS.NICK };

                CursorLoader cursorLoader = new CursorLoader(getActivity(),
                        ServersContentProvider.CONTENT_URI, projection, null, null, null);
                return cursorLoader;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                List<String> serverList = new ArrayList<String>();
                cursor.move(-1);
                while (cursor.moveToNext()) {
                    String server = cursor.getString(cursor.getColumnIndex(ServersTable.COLUMNS.SERVER));
                    serverList.add(server);
                }
                cursor.close();
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, serverList);
                serverSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                serverSpinner.setAdapter(serverSpinnerAdapter);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        };
    }


}
