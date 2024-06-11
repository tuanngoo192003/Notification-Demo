package com.example.notificationdemo;

import static android.content.ContentValues.TAG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.RemoteInput;

public class MessageReplyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            CharSequence replyText = remoteInput.getCharSequence("key_text_reply");
            int conversationId = intent.getIntExtra("conversation_id", -1);

            // Handle the reply text and conversation ID
            Log.d(TAG, "Reply received: " + replyText + " for conversation: " + conversationId);

            // TODO: Process the reply text, such as sending it to your server or updating the UI
        }
    }
}
