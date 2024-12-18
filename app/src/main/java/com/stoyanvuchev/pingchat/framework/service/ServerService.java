package com.stoyanvuchev.pingchat.framework.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerService extends Service {

    private Server server;

    @Override
    public void onCreate() {
        super.onCreate();
        server = new Server();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(() -> server.startServer()).start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        server.shutdown();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class Server {

        private final static String TAG = "ServerTag";
        private final static int PORT = 11221;
        private ServerSocket serverSocket;
        private final ExecutorService executorService; // Used for handling multiple clients.

        public Server() {
            executorService = Executors.newFixedThreadPool(4); // Support for up to 4 clients.
        }

        public void startServer() {
            try {

                serverSocket = new ServerSocket(PORT);
                System.out.println("Server started. Waiting for clients ...");

                while (!serverSocket.isClosed()) {

                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected: " + clientSocket.getInetAddress());

                    executorService.execute(new Server.ClientHandler(clientSocket));

                }

            } catch (Exception e) {
                Log.e(TAG, Objects.requireNonNull(e.getMessage()));
            }
        }

        public void shutdown() {
            try {
                serverSocket.close();
            } catch (IOException e) {
                Log.e(TAG, Objects.requireNonNull(e.getMessage()));
            }
            executorService.shutdown();
        }

        private class ClientHandler implements Runnable {

            private final Socket clientSocket;

            public ClientHandler(Socket socket) {
                this.clientSocket = socket;
            }

            @Override
            public void run() {
                try (
                        BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
                ) {

                    String message;
                    while ((message = input.readLine()) != null) {

                        broadcastMessage(message);

                    }

                } catch (IOException e) {

                    Log.e(TAG, Objects.requireNonNull(e.getMessage()));

                } finally {

                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                    }

                }
            }

        }

        private void broadcastMessage(String msg) {
            Intent intent = new Intent("NEW_MESSAGE");
            intent.putExtra("message", msg);
            sendBroadcast(intent);
        }

    }

}