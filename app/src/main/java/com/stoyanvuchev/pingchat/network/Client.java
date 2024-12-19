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
        // Starting a Thread to listen for messages from the server.
        new Thread(() -> {
            try {

                socket = new Socket(serverAddress, port);
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);

                String msg;
                while (true) {
                    msg = input.readLine();
                    if (msg != null) {
                        System.out.println(TAG + ": Message From Server: " + msg);
                    }
                    Thread.sleep(10);
                }

            } catch (IOException e) {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void sendMessage(String message) {
        if (output != null) {
            output.write(message);
            output.flush();
        } else {
            Log.e(TAG, "Error: Cannot send a message: The output doesn't exist!");
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
