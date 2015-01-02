package ircu.navjotpanesar.com.ircu.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pircbotx.Channel;

import java.util.ArrayList;

import ircu.navjotpanesar.com.ircu.R;
import ircu.navjotpanesar.com.ircu.adapters.ChannelListAdapter;
import ircu.navjotpanesar.com.ircu.adapters.DividerItemDecoration;
import ircu.navjotpanesar.com.ircu.models.ChannelListItem;
import ircu.navjotpanesar.com.ircu.utils.ChatLogger;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ircu.navjotpanesar.com.ircu.fragments.ChannelListFragment.OnChannelSwitchListener} interface
 * to handle interaction events.
 * Use the {@link ChannelListFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ChannelListFragment extends BaseChatFragment {
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

        return rootView;
    }

    private void setupChannelListView(View rootView) {
        channelRecyclerView = (RecyclerView)rootView.findViewById(R.id.channel_list);
        channelListAdapter = new ChannelListAdapter(new ArrayList<ChannelListItem>());
        channelRecyclerView.setHasFixedSize(true);
        channelRecyclerView.setAdapter(channelListAdapter);
        channelRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        channelRecyclerView.setItemAnimator(new DefaultItemAnimator());
        channelRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public void handleChannelJoin(Channel channel) {
        super.handleChannelJoin(channel);
        channelListAdapter.append(new ChannelListItem(channel));
        ChatLogger.v("New channel to list");
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void switchChannel(Channel channel) {
        if (mListener != null) {
            mListener.channelSwitch(channel);
        }
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
        public void channelSwitch(Channel channel);
    }

}
