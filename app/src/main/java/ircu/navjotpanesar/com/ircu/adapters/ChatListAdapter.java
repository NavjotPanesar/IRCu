package ircu.navjotpanesar.com.ircu.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ircu.navjotpanesar.com.ircu.R;
import ircu.navjotpanesar.com.ircu.models.ChatMessage;

/**
 * Created by Navjot on 11/28/2014.
 */
public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private List<ChatMessage> messageList;

    public ChatListAdapter(List<ChatMessage> messageList) {
        this.messageList = messageList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ChatMessage message = messageList.get(position);
        viewHolder.messageView.setText(message.getMessage());
        viewHolder.authorView.setText(message.getAuthor());
        viewHolder.itemView.setTag(message);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView messageView;
        public TextView authorView;

        public ViewHolder(View itemView) {
            super(itemView);
            messageView = (TextView) itemView.findViewById(R.id.chat_list_item_message);
            authorView = (TextView) itemView.findViewById(R.id.chat_list_item_author);
        }
    }

    public void add(ChatMessage message, int position) {
        messageList.add(position, message);
        notifyItemInserted(position);
    }

    public void append(ChatMessage message){
        messageList.add(message);
        notifyItemInserted(messageList.size() - 1);
    }

    public void remove(ChatMessage message) {
        int position = messageList.indexOf(message);
        messageList.remove(position);
        notifyItemRemoved(position);
    }
}
