package ircu.navjotpanesar.com.ircu.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ircu.navjotpanesar.com.ircu.R;
import ircu.navjotpanesar.com.ircu.callbacks.OnChannelListItemSelectedListener;
import ircu.navjotpanesar.com.ircu.fragments.ChannelListFragment;
import ircu.navjotpanesar.com.ircu.models.ChannelListItem;
import ircu.navjotpanesar.com.ircu.pircbot.ChannelItem;

/**
 * Created by Navjot on 1/1/2015.
 */
public class ChannelListAdapter extends RecyclerView.Adapter<ChannelListAdapter.ViewHolder> {

    private List<ChannelListItem> messageList;
    private ChannelListFragment.OnChannelSwitchListener onChannelSwitchListener;

    public ChannelListAdapter(List<ChannelListItem> messageList, ChannelListFragment.OnChannelSwitchListener onChannelSwitchListener) {
        this.messageList = messageList;
        this.onChannelSwitchListener = onChannelSwitchListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_list_item, parent, false);
        return new ViewHolder(v, onChannelListItemSelectedListener);
    }

    private OnChannelListItemSelectedListener onChannelListItemSelectedListener = new OnChannelListItemSelectedListener() {
        @Override
        public void onChannelListItemSelected(int position) {
            //TODO: remove the need for this conversion
            if(position < 0 || position >= messageList.size()){
                return;
            }
            ChannelListItem selectedItem = messageList.get(position);
            if(selectedItem != null){
                ChannelItem channelItem = new ChannelItem(selectedItem.getChannelName(), selectedItem.getServerName());
                onChannelSwitchListener.channelSwitch(channelItem);
            }

        }
    };

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ChannelListItem channel = messageList.get(position);
        viewHolder.serverView.setText(channel.getServerName());
        viewHolder.channelView.setText(channel.getChannelName());
        viewHolder.itemView.setTag(channel.getServerName() + "/" + channel.getChannelName());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView serverView;
        public TextView channelView;
        private OnChannelListItemSelectedListener onChannelListItemSelectedListener;

        public ViewHolder(View itemView, OnChannelListItemSelectedListener onChannelListItemSelectedListener) {
            super(itemView);
            this.onChannelListItemSelectedListener = onChannelListItemSelectedListener;
            serverView = (TextView) itemView.findViewById(R.id.channel_list_servername);
            channelView = (TextView) itemView.findViewById(R.id.channel_list_channelname);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            if(this.onChannelListItemSelectedListener != null){
                int position = getPosition();
                onChannelListItemSelectedListener.onChannelListItemSelected(position);
            }
        }
    }

    public void add(ChannelListItem channel, int position) {
        messageList.add(position, channel);
        notifyItemInserted(position);
    }

    public void append(ChannelListItem channel){
        messageList.add(channel);
        notifyItemInserted(messageList.size() - 1);
    }

    public void remove(ChannelListItem channel) {
        int position = messageList.indexOf(channel);
        messageList.remove(position);
        notifyItemRemoved(position);
    }

    public void setData(List<ChannelListItem> data){
        this.messageList = data;
        notifyDataSetChanged();
    }
}
