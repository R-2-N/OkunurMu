package com.ungratz.okunurmu.Activities;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ungratz.okunurmu.databinding.RecievedMessageBinding;
import com.ungratz.okunurmu.databinding.SentMessageBinding;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ChatMessage> chatMessages;
    private final String senderId;

    private final int RECEIVED_VIEW = 1;
    private final int SENT_VIEW = 2;

    public ChatAdapter(List<ChatMessage> chatMessages, String senderId) {
        this.chatMessages = chatMessages;
        this.senderId = senderId;
    }

    public int getViewType(int position) {
        if(chatMessages.get(position).getSender().equals(senderId)) {
            return SENT_VIEW;
        }
        else {
            return RECEIVED_VIEW;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == SENT_VIEW) {
            return new SentMessageView(
                    SentMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }
        else {
            return new RecieverMessageView(
                    RecievedMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == SENT_VIEW) {
            ((SentMessageView) holder).setData(chatMessages.get(position));
        }
        else {
            ((RecieverMessageView) holder).setData(chatMessages.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    static class SentMessageView extends RecyclerView.ViewHolder {

        private final SentMessageBinding binding;

        SentMessageView(SentMessageBinding sentMessageBinding) {
            super(sentMessageBinding.getRoot());
            binding = sentMessageBinding;
        }

        void setData(ChatMessage ChatMessage) {
            binding.textMessage.setText(ChatMessage.getMessage());
            binding.date.setText(ChatMessage.getDate());
        }
    }

    static class RecieverMessageView extends RecyclerView.ViewHolder {

        private final RecievedMessageBinding binding;

        RecieverMessageView(RecievedMessageBinding recievedMessageBinding) {
            super(recievedMessageBinding.getRoot());
            binding = recievedMessageBinding;
        }

        void setData(ChatMessage ChatMessage) {
            binding.textMessage.setText(ChatMessage.getMessage());
            binding.date.setText(ChatMessage.getDate());
        }
    }
}
