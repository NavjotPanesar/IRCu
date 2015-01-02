package ircu.navjotpanesar.com.ircu.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ircu.navjotpanesar.com.ircu.R;
import ircu.navjotpanesar.com.ircu.models.ChannelListItem;

/**
 * Created by Navjot on 1/1/2015.
 */
public class ChannelListAdapter extends RecyclerView.Adapter<ChannelListAdapter.ViewHolder> {

    private List<ChannelListItem> messageList;

    public ChannelListAdapter(List<ChannelListItem> messageList) {
        this.messageList = messageList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_list_item, parent, false);
        return new ViewHolder(v);
    }

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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView serverView;
        public TextView channelView;

        public ViewHolder(View itemView) {
            super(itemView);
            serverView = (TextView) itemView.findViewById(R.id.channel_list_servername);
            channelView = (TextView) itemView.findViewById(R.id.channel_list_channelname);
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
}
