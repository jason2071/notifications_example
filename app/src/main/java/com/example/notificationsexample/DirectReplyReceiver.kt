package com.example.notificationsexample

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.RemoteInput

class DirectReplyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val remoteInput = RemoteInput.getResultsFromIntent(intent)
        if (remoteInput != null) {
            val replyText = remoteInput.getCharSequence("key_text_reply")
            val answer = Message(replyText, null)

            MainActivity.MESSAGE.add(answer)
        }
    }
}