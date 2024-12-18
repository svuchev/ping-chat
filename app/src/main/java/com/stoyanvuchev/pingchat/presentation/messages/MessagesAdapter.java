package com.stoyanvuchev.pingchat.presentation.messages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.stoyanvuchev.pingchat.R;
import com.stoyanvuchev.pingchat.domain.model.Message;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int VIEW_TYPE_SENT = 1;
    private final static int VIEW_TYPE_RECEIVED = 2;

    private List<Message> messagesList;

    public MessagesAdapter(List<Message> messagesList) {
        this.messagesList = messagesList;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messagesList.get(position);
        return message.isSent() ? VIEW_TYPE_SENT : VIEW_TYPE_RECEIVED;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_SENT) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_item_sender, parent, false);

            return new SenderMessageViewHolder(view);

        } else {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_item_receiver, parent, false);

            return new ReceiverMessageViewHolder(view);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Message message = messagesList.get(position);

        if (holder instanceof SenderMessageViewHolder) {
            ((SenderMessageViewHolder) holder).bind(message);
        } else if (holder instanceof ReceiverMessageViewHolder) {
            ((ReceiverMessageViewHolder) holder).bind(message);
        }

    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    static class SenderMessageViewHolder extends RecyclerView.ViewHolder {

        TextView msgContentTv;

        SenderMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            msgContentTv = itemView.findViewById(R.id.senderMsgContentTv);
        }

        void bind(Message message) {
            msgContentTv.setText(message.getContent());
        }

    }

    static class ReceiverMessageViewHolder extends RecyclerView.ViewHolder {

        TextView msgContentTv;

        ReceiverMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            msgContentTv = itemView.findViewById(R.id.receiverMsgContentTv);
        }

        void bind(Message message) {
            msgContentTv.setText(message.getContent());
        }

    }

}
