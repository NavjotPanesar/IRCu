package ircu.navjotpanesar.com.ircu.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ircu.navjotpanesar.com.ircu.R;
import ircu.navjotpanesar.com.ircu.models.BaseMessage;
import ircu.navjotpanesar.com.ircu.models.ChatMessage;
import ircu.navjotpanesar.com.ircu.models.SystemMessage;
import ircu.navjotpanesar.com.ircu.utils.CachedIdenticonStorage;
import ircu.navjotpanesar.com.ircu.utils.IdenticonFactory;

/**
 * Created by Navjot on 11/28/2014.
 */
public class ChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BaseMessage> messageList;
    private CachedIdenticonStorage identiconStorage;

    public ChatListAdapter(List<BaseMessage> messageList, Activity activity) {
        this.messageList = messageList;
        IdenticonFactory identiconFactory = new IdenticonFactory(activity, 48, 48);
        identiconStorage = new CachedIdenticonStorage(identiconFactory);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = 0;
        if(viewType == BaseMessage.MessageType.CHAT.ordinal()){
            layoutId = R.layout.chat_list_item;
            View v = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            return new ChatMessageViewHolder(v);
        } else {
            layoutId = R.layout.system_chat_list_item;
            View v = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            return new SystemMessageViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        BaseMessage baseMessage = messageList.get(position);
        if(baseMessage.getMessageType() == BaseMessage.MessageType.CHAT){
            ChatMessage message = (ChatMessage) baseMessage;
            ChatMessageViewHolder chatViewHolder = (ChatMessageViewHolder) viewHolder;
            chatViewHolder.messageView.setText(message.getMessage());
            chatViewHolder.authorView.setText(message.getAuthor());
            chatViewHolder.imageView.setImageBitmap(identiconStorage.getImage(message.getAuthor()));
            viewHolder.itemView.setTag(message);
        } else if (baseMessage.getMessageType() == BaseMessage.MessageType.SYSTEM){
            SystemMessage message = (SystemMessage) baseMessage;
            SystemMessageViewHolder systemViewHolder = (SystemMessageViewHolder) viewHolder;
            systemViewHolder.messageView.setText(message.getMessage());
            systemViewHolder.infoView.setText(message.getInfoType());
            viewHolder.itemView.setTag(message);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).getMessageType().ordinal();
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class ChatMessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageView;
        public TextView authorView;
        public ImageView imageView;

        public ChatMessageViewHolder(View itemView) {
            super(itemView);
            messageView = (TextView) itemView.findViewById(R.id.chat_list_item_message);
            authorView = (TextView) itemView.findViewById(R.id.chat_list_item_author);
            imageView = (ImageView) itemView.findViewById(R.id.chat_list_item_image);
        }
    }

    public static class SystemMessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageView;
        public TextView infoView;

        public SystemMessageViewHolder(View itemView) {
            super(itemView);
            messageView = (TextView) itemView.findViewById(R.id.chat_list_item_message);
            infoView = (TextView) itemView.findViewById(R.id.chat_list_item_infotype);
        }
    }

    public void add(BaseMessage message, int position) {
        messageList.add(position, message);
        notifyItemInserted(position);
    }

    public void append(BaseMessage message){
        messageList.add(message);
        notifyItemInserted(messageList.size() - 1);
    }

    public void remove(BaseMessage message) {
        int position = messageList.indexOf(message);
        messageList.remove(position);
        notifyItemRemoved(position);
    }

    public void setMessageList(List<BaseMessage> messageList){
        // copy, it over; this.messageList is just an array for displaying the chat items.
        // we don't want additions to it to also mutate the messageList within the channel objects
        this.messageList = new ArrayList<>(messageList);
        notifyDataSetChanged();
    }
}
