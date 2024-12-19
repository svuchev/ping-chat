package com.stoyanvuchev.pingchat.framework.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MessagesReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // todo: Override this method.
        String msg = intent.getStringExtra("message");
        System.out.println("MessagesReceiver: Received a message: " + msg);
    }

}
