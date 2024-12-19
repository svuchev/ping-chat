package com.stoyanvuchev.pingchat.presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.stoyanvuchev.pingchat.R;
import com.stoyanvuchev.pingchat.databinding.FragmentFirstBinding;
import com.stoyanvuchev.pingchat.framework.service.ServerService;
import com.stoyanvuchev.pingchat.util.IPAddressUtil;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.startServerBtn.setOnClickListener(v -> startServer());
        binding.stopServerBtn.setOnClickListener(v -> stopServer());
        binding.connectBtn.setOnClickListener(v -> connect());
    }

    @SuppressLint("SetTextI18n")
    private void startServer() {

        // Start the Server Service.
        Intent intent = new Intent(requireActivity(), ServerService.class);
        requireActivity().startService(intent);
        Toast.makeText(requireContext(), "Server started", Toast.LENGTH_SHORT).show();

        // Display the Server IPv4 Address & Port.
        String ipv4Address = IPAddressUtil.getLocalIPv4Address();
        final int PORT = 9090;
        binding.serverIPAddressTextView.setText(ipv4Address + " : " + PORT);

    }

    @SuppressLint("SetTextI18n")
    private void stopServer() {

        // Stop the Server Service.
        Intent intent = new Intent(requireActivity(), ServerService.class);
        requireActivity().stopService(intent);

        // Notify the user that the Server is not running.
        binding.serverIPAddressTextView.setText("Server not running");

    }

    private void connect() {

        String ipAddress = binding.serverIPAddressEditText.getText().toString();
        String port = binding.serverPortEditText.getText().toString();

        if (!ipAddress.isBlank() && !port.isBlank()) {
            navigateToMessagesFragment(false, ipAddress, Integer.parseInt(port));
        }

    }

    private void navigateToMessagesFragment(boolean isServer, String ipAddress, int port) {

        Bundle bundle = new Bundle();
        bundle.putBoolean("isServer", isServer);
        bundle.putString("ipAddress", ipAddress);
        bundle.putInt("port", port);

        NavHostFragment.findNavController(FirstFragment.this)
                .navigate(R.id.action_FirstFragment_to_MessagesFragment, bundle);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}