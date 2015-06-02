package ircu.navjotpanesar.com.ircu.adapters;

import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.StateSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ircu.navjotpanesar.com.ircu.R;
import ircu.navjotpanesar.com.ircu.callbacks.OnChannelListItemSelectedListener;
import ircu.navjotpanesar.com.ircu.fragments.ChannelListFragment;
import ircu.navjotpanesar.com.ircu.pircbot.ChannelItem;

/**
 * Created by Navjot on 1/1/2015.
 */
public class ChannelListAdapter extends RecyclerView.Adapter<ChannelListAdapter.ViewHolder> {

    private List<ChannelItem> messageList;
    private ChannelListFragment.OnChannelSwitchListener onChannelSwitchListener;
    private int selectedPos = -1;

    public ChannelListAdapter(List<ChannelItem> messageList, ChannelListFragment.OnChannelSwitchListener onChannelSwitchListener) {
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
            if(position < 0 || position >= messageList.size()){
                return;
            }

            int oldSelectedPos = selectedPos;
            selectedPos = position;
            notifyItemChanged(oldSelectedPos);
            notifyItemChanged(selectedPos);

            ChannelItem selectedItem = messageList.get(position);
            if(selectedItem != null){
                onChannelSwitchListener.channelSwitch(selectedItem);
            }

        }
    };

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ChannelItem channel = messageList.get(position);
        viewHolder.serverView.setText(channel.getServer());
        viewHolder.channelView.setText(channel.getChannelName());
        viewHolder.itemView.setTag(channel.getServer() + "/" + channel.getChannelName());
        viewHolder.itemView.setSelected(selectedPos == position);
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

            ColorDrawable colorDrawableSelected =
                    new ColorDrawable(itemView.getResources().getColor(R.color.item_state_selected_color));

            // create StateListDrawable object and define its states
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_selected}, colorDrawableSelected);
            stateListDrawable.addState(StateSet.WILD_CARD, null);
            itemView.setBackground(stateListDrawable);
        }


        @Override
        public void onClick(View v) {
            if(this.onChannelListItemSelectedListener != null){
                int position = getPosition();
                onChannelListItemSelectedListener.onChannelListItemSelected(position);
            }
        }
    }

    public void add(ChannelItem channel, int position) {
        messageList.add(position, channel);
        notifyItemInserted(position);
    }

    public void append(ChannelItem channel){
        messageList.add(channel);
        notifyItemInserted(messageList.size() - 1);
    }

    public void remove(ChannelItem channel) {
        int position = messageList.indexOf(channel);
        messageList.remove(position);
        notifyItemRemoved(position);
    }

    public void setData(List<ChannelItem> data){
        this.messageList = data;
        notifyDataSetChanged();
    }
}
