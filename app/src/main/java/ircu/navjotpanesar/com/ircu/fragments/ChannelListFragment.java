package ircu.navjotpanesar.com.ircu.fragments;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import ircu.navjotpanesar.com.ircu.R;
import ircu.navjotpanesar.com.ircu.adapters.ChannelListAdapter;
import ircu.navjotpanesar.com.ircu.adapters.DividerItemDecoration;
import ircu.navjotpanesar.com.ircu.callbacks.OnDialogSuccessListener;
import ircu.navjotpanesar.com.ircu.contentproviders.ChannelsContentProvider;
import ircu.navjotpanesar.com.ircu.database.ChannelsTable;
import ircu.navjotpanesar.com.ircu.pircbot.ChannelItem;
import ircu.navjotpanesar.com.ircu.utils.IdenticonFactory;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ircu.navjotpanesar.com.ircu.fragments.ChannelListFragment.OnChannelSwitchListener} interface
 * to handle interaction events.
 * Use the {@link ChannelListFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ChannelListFragment extends Fragment {
    private OnChannelSwitchListener mListener;
    private RecyclerView channelRecyclerView;
    private ChannelListAdapter channelListAdapter;

    public static ChannelListFragment newInstance() {
        ChannelListFragment fragment = new ChannelListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public ChannelListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_channel_list, container, false);
        setupChannelListView(rootView);

        Button addNewButton = (Button) rootView.findViewById(R.id.fragment_channel_add_new_button);
        addNewButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                AddChannelDialogFragment.newInstance(onAddNewChannelDialogSuccessListener).show(getFragmentManager(), "add_new_channel");
            }
        });


        ImageView imageView = (ImageView) rootView.findViewById(R.id.navigation_header_imageview);
        TextView usernameView = (TextView) rootView.findViewById(R.id.navigation_header_username);

        String username = "Testerino2311";
        IdenticonFactory identiconFactory = new IdenticonFactory(getActivity(), 70, 70);
        usernameView.setText(username);
        imageView.setImageBitmap(identiconFactory.getBitmap(username));

        return rootView;
    }

    private OnDialogSuccessListener onAddNewChannelDialogSuccessListener = new OnDialogSuccessListener() {
        @Override
        public void onSuccess(Intent intent) {
            Bundle extras = intent.getExtras();
            String server = extras.getString("server", "");
            String channel = extras.getString("channel", "");

            saveNewChannel(channel, server);
        }
    };

    private void setupChannelListView(View rootView) {
        channelRecyclerView = (RecyclerView) rootView.findViewById(R.id.channel_list);
        channelListAdapter = new ChannelListAdapter(new ArrayList<ChannelItem>(), onChannelSwitchListener);
        channelRecyclerView.setHasFixedSize(true);
        channelRecyclerView.setAdapter(channelListAdapter);
        channelRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        channelRecyclerView.setItemAnimator(new DefaultItemAnimator());
        channelRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        channelRecyclerView.setClickable(true);
        channelRecyclerView.setFocusable(true);

        loadData();
    }
    
    private OnChannelSwitchListener onChannelSwitchListener = new OnChannelSwitchListener() {
        @Override
        public void channelSwitch(ChannelItem channel) {
            mListener.channelSwitch(channel);
        }
    };

    //call when user adds new channel
    private void saveNewChannel(String channel, String server){
        ChannelItem channelListItem = new ChannelItem(channel, server);
        channelListAdapter.append(channelListItem);
    }

    private void loadData() {
        getLoaderManager().initLoader(0, null, getContentLoaderCallback());
    }

    private LoaderManager.LoaderCallbacks<Cursor> getContentLoaderCallback(){
        return new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                String[] projection = { ChannelsTable.COLUMNS.ID, ChannelsTable.COLUMNS.CHANNEL, ChannelsTable.COLUMNS.SERVER };

                CursorLoader cursorLoader = new CursorLoader(getActivity(),
                        ChannelsContentProvider.CONTENT_URI, projection, null, null, null);
                return cursorLoader;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                List<ChannelItem> messageList = new ArrayList<ChannelItem>();
                cursor.move(-1);
                while (cursor.moveToNext()) {
                    String channel = cursor.getString(cursor.getColumnIndex(ChannelsTable.COLUMNS.CHANNEL));
                    String server = cursor.getString(cursor.getColumnIndex(ChannelsTable.COLUMNS.SERVER));
                    ChannelItem message = new ChannelItem(channel, server);
                    messageList.add(message);
                }
                cursor.close();
                channelListAdapter.setData(messageList);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnChannelSwitchListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnChannelSwitchListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnChannelSwitchListener {
        // TODO: Update argument type and name
        public void channelSwitch(ChannelItem channel);
    }

}
