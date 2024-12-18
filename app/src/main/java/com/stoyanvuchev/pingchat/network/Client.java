package com.stoyanvuchev.pingchat.network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

public class Client {

    private final static String TAG = "ClientTag";
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public void connectToServer(String serverAddress, int port) {
        try {

            socket = new Socket(serverAddress, port);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            // Starting a Thread to listen for messages from the server.
            new Thread(() -> {
                try {

                    String msg;
                    while ((msg = input.readLine()) != null) {
                        System.out.println("Server: " + msg);
                    }

                } catch (IOException e) {
                    Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                }
            }).start();

        } catch (IOException e) {
            Log.d(TAG, Objects.requireNonNull(e.getMessage()));
        }
    }

    public void sendMessage(String message) {
        if (output != null) {
            output.println(message);
        }
    }

    public void disconnect() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            Log.d(TAG, Objects.requireNonNull(e.getMessage()));
        }
    }

}
