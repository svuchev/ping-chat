package com.stoyanvuchev.pingchat.presentation.messages;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.stoyanvuchev.pingchat.databinding.FragmentMessagesBinding;
import com.stoyanvuchev.pingchat.domain.model.Message;
import com.stoyanvuchev.pingchat.network.Client;

import java.util.ArrayList;
import java.util.List;

public class MessagesFragment extends Fragment {

    private String ipAddress;
    private int port;

    private FragmentMessagesBinding binding;

    private List<Message> messagesList = new ArrayList<>();
    private MessagesAdapter messagesAdapter;

    private Client client;
    private final static String TAG = "MessagesFragmentTag";

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        if (getArguments() != null) {
            ipAddress = getArguments().getString("ipAddress");
            port = getArguments().getInt("port");
        } else {
            requireActivity().onBackPressed();
        }

        binding = FragmentMessagesBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        messagesAdapter = new MessagesAdapter(messagesList);
        binding.messagesRecyclerView.setAdapter(messagesAdapter);

        // Initialize the Client and connect to the server.
        client = new Client();
        connectToServer();

        // Send a message.
        binding.messagesSendBtn.setOnClickListener(v -> sendMessage());

    }

    @SuppressLint("NotifyDataSetChanged")
    private void connectToServer() {

        try {

            client = new Client();
            client.connectToServer(ipAddress, port);

        } catch (Exception e) {

            Log.e(TAG, "Error: " + e.getMessage());

        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private void sendMessage() {

        String msg = binding.messagesEditText.getText().toString();

        if (!msg.isBlank()) {

            messagesList.add(new Message(msg, true));
            messagesAdapter.notifyDataSetChanged();
            binding.messagesEditText.setText("");
            binding.messagesEditText.clearFocus();

            client.sendMessage(msg);

        }

    }

    private final BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra("message");
            if (msg != null) {
                messagesList.add(new Message(msg, false));
                messagesAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().registerReceiver(messageReceiver, new IntentFilter("NEW_MESSAGE"), Context.RECEIVER_NOT_EXPORTED);
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().unregisterReceiver(messageReceiver);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        client.disconnect();
        binding = null;
    }

}