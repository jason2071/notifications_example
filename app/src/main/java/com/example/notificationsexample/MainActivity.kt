package com.example.notificationsexample

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.app.RemoteInput
import android.support.v4.media.session.MediaSessionCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.notificationsexample.App.CHANNEL_1_ID
import com.example.notificationsexample.App.CHANNEL_2_ID
import kotlinx.android.synthetic.main.activity_main.*
import android.os.SystemClock




class MainActivity : AppCompatActivity() {

    lateinit var notificationManager: NotificationManagerCompat
    lateinit var mediaSession: MediaSessionCompat

    companion object {
        val MESSAGE: ArrayList<Message> = ArrayList()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationManager = NotificationManagerCompat.from(this@MainActivity)

        // start chat notification not working
        mediaSession = MediaSessionCompat(this@MainActivity, "tag")
        MESSAGE.add(Message("Good morning!", "Jim"))
        MESSAGE.add(Message("Hello", null))
        MESSAGE.add(Message("Hi!", "Jenny"))
        // end chat notification not working

        btnChannel1.setOnClickListener {
            sendOnChannelPicture()
        }

        btnChannel2.setOnClickListener {
            sendOnChannelGroup()
        }

    }

    private fun sendOnChannelGroup() {
        val title1 = "Title 1"
        val message1 = "Message 1"
        val title2 = "Title 2"
        val message2 = "Message 2"
        val title3 = "Title 3"
        val message3 = "Message 3"

        val notification1 = NotificationCompat.Builder(this, CHANNEL_2_ID)
            .setSmallIcon(R.drawable.ic_filter_2_black_24dp)
            .setContentTitle(title1)
            .setContentText(message1)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setGroup("example_group")
            .build()

        val notification2 = NotificationCompat.Builder(this, CHANNEL_2_ID)
            .setSmallIcon(R.drawable.ic_filter_2_black_24dp)
            .setContentTitle(title2)
            .setContentText(message2)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setGroup("example_group")
            .build()

        val notification3 = NotificationCompat.Builder(this, CHANNEL_2_ID)
            .setSmallIcon(R.drawable.ic_filter_2_black_24dp)
            .setContentTitle(title3)
            .setContentText(message3)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setGroup("example_group")
            .build()

        val summaryNotification= NotificationCompat.Builder(this, CHANNEL_2_ID)
            .setSmallIcon(R.drawable.ic_reply)
            .setStyle(NotificationCompat.InboxStyle()
                .addLine("$title2 $message2")
                .addLine("$title3 $message3")
                .addLine("$title1 $message1")
                .setBigContentTitle("3 new messages")
                .setSummaryText("user@example.com"))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setGroup("example_group")
            .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
            .setGroupSummary(true)
            .build()

        SystemClock.sleep(5000)
        notificationManager.notify(2, notification1)

        SystemClock.sleep(5000)
        notificationManager.notify(3, notification2)

        SystemClock.sleep(5000)
        notificationManager.notify(4, notification3)

        SystemClock.sleep(5000)
        notificationManager.notify(5, summaryNotification)
    }

    private fun sendOnChannelProgressBar() {
        val progressMax = 100

        val notification = NotificationCompat.Builder(this, CHANNEL_1_ID)
            .setSmallIcon(R.drawable.ic_file_download)
            .setContentTitle("Document")
            .setContentText("Downloading")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setProgress(progressMax, 0, true)

        notificationManager.notify(1, notification.build())

        Thread(Runnable {
            SystemClock.sleep(2000)
            var progress = 0
            while (progress <= progressMax) {
                SystemClock.sleep(1000)
                progress += 20
            }
            notification.setContentText("Download completed")
                .setProgress(0, 0, false)
                .setOngoing(false)
            notificationManager.notify(1, notification.build())
        }).start()

    }

    private fun sendOnChannelChat() {
        val activityIntent = Intent(this@MainActivity, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(this@MainActivity, 0, activityIntent, 0)

        val remoteInput = RemoteInput.Builder("key_text_reply")
            .setLabel("Your answer...")
            .build()

        val replyIntent = Intent(this@MainActivity, DirectReplyReceiver::class.java)
        val replyPendingIntent = PendingIntent.getBroadcast(this@MainActivity, 0, replyIntent, 0)

        val replayAction: NotificationCompat.Action = NotificationCompat.Action.Builder(
            R.drawable.ic_reply
            , "Reply"
            , replyPendingIntent
        ).addRemoteInput(remoteInput).build()

        val messagingStyle: NotificationCompat.MessagingStyle = NotificationCompat.MessagingStyle("Me")
        messagingStyle.conversationTitle = "Group Chat"

        for (chatMessage in MESSAGE) {
            val notificationMessage = NotificationCompat.MessagingStyle.Message(
                chatMessage.text,
                chatMessage.timestamp,
                chatMessage.sender
            )
            messagingStyle.addMessage(notificationMessage)
        }

        val notification = NotificationCompat.Builder(this, CHANNEL_1_ID)
            .setSmallIcon(R.drawable.ic_filter_1_black_24dp)
            .setStyle(messagingStyle)
            .addAction(replayAction)
            .setColor(Color.GREEN)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setColor(Color.GREEN)
            .setContentIntent(contentIntent)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .build()

        notificationManager.notify(1, notification)
    }

    private fun sendOnChannelPlayControls() {
        val title = edit_text_title.text.toString()
        val message = edit_text_message.text.toString()

        val artwork = BitmapFactory.decodeResource(resources, R.drawable.tesla_cat_300)

        val notification = NotificationCompat.Builder(this, CHANNEL_2_ID)
            .setSmallIcon(R.drawable.ic_filter_2_black_24dp)
            .setContentTitle(title)
            .setContentText(message)
            .setLargeIcon(artwork)
            .addAction(R.drawable.ic_dislike, "Dislike", null)
            .addAction(R.drawable.ic_previous, "Previous", null)
            .addAction(R.drawable.ic_pause, "Pause", null)
            .addAction(R.drawable.ic_next, "Next", null)
            .addAction(R.drawable.ic_like, "Like", null)
            .setStyle(
                android.support.v4.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(1, 2, 3)
                    .setMediaSession(mediaSession.sessionToken)
            )
            .setSubText("Sub Text")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .build()

        notificationManager.notify(2, notification)
    }

    private fun sendOnChannelLargeIcon() {
        val title = edit_text_title.text.toString()
        val message = edit_text_message.text.toString()

        val activityIntent = Intent(this, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(this@MainActivity, 0, activityIntent, 0)

        val broadcastIntent = Intent(this@MainActivity, NotificationReceiver::class.java)
        broadcastIntent.putExtra("toastMessage", message)
        val actionIntent =
            PendingIntent.getBroadcast(this@MainActivity, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.tesla_cat_300)

        val notification = NotificationCompat.Builder(this, CHANNEL_1_ID)
            .setSmallIcon(R.drawable.ic_filter_1_black_24dp)
            .setContentTitle(title)
            .setContentText(message)
            .setLargeIcon(largeIcon)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(getString(R.string.long_dummy_text))
                    .setBigContentTitle("Big Content Title")
                    .setSummaryText("Summery Text")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setColor(Color.BLUE)
            .setContentIntent(contentIntent)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .addAction(R.mipmap.ic_launcher, "Toast", actionIntent)
            .build()

        SystemClock.sleep(10000)
        notificationManager.notify(1, notification)
    }

    private fun sendOnChannelPicture() {
        val title = edit_text_title.text.toString()
        val message = edit_text_message.text.toString()

        val activityIntent = Intent(this, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(this@MainActivity, 0, activityIntent, 0)

        val picture = BitmapFactory.decodeResource(resources, R.drawable.tesla_cat)

        val notification = NotificationCompat.Builder(this, CHANNEL_1_ID)
            .setSmallIcon(R.drawable.ic_filter_1_black_24dp)
            .setContentTitle(title)
            .setContentText(message)
            .setLargeIcon(picture)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(picture)
                    .bigLargeIcon(null)
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setColor(Color.BLUE)
            .setContentIntent(contentIntent)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .build()

        SystemClock.sleep(10000)
        notificationManager.notify(1, notification)
    }

    private fun sendOnChannelMultiLines() {
        val title = edit_text_title.text.toString()
        val message = edit_text_message.text.toString()

        val notification = NotificationCompat.Builder(this, CHANNEL_2_ID)
            .setSmallIcon(R.drawable.ic_filter_2_black_24dp)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(
                NotificationCompat.InboxStyle()
                    .addLine("This is line 1")
                    .addLine("This is line 2")
                    .addLine("This is line 3")
                    .addLine("This is line 4")
                    .addLine("This is line 5")
                    .addLine("This is line 6")
                    .addLine("This is line 7")
                    .setBigContentTitle("Big Content Title")
                    .setSummaryText("Summery Text")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .build()

        notificationManager.notify(2, notification)
    }

    fun log(s: String) {
        Log.d("MainActivityAAA", "debug ::: $s")
    }
}
