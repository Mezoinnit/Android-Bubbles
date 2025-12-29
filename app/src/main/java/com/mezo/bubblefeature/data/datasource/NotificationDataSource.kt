package com.mezo.bubblefeature.data.datasource

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.app.RemoteInput
import androidx.core.content.LocusIdCompat
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.mezo.bubblefeature.BubbleActivity
import com.mezo.bubblefeature.MainActivity
import com.mezo.bubblefeature.R
import com.mezo.bubblefeature.domain.model.Message

class NotificationDataSource(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "chat_channel"
        const val SHORTCUT_ID = "contact_123"
        const val NOTIFICATION_ID = 1
        const val KEY_TEXT_REPLY = "key_text_reply"
    }

    private val notificationManager = 
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val cachedIcon: IconCompat by lazy {
        IconCompat.createWithResource(context, R.drawable.profile)
    }

    init {
        createNotificationChannel()
    }

    fun showBubbleNotification(messages: List<Message>) {
        val person = Person.Builder()
            .setName(context.getString(R.string.contact_name))
            .setIcon(cachedIcon)
            .setImportant(true)
            .build()

        val shortcutIntent = Intent(context, MainActivity::class.java).apply {
            action = Intent.ACTION_VIEW
        }

        val shortcut = ShortcutInfoCompat.Builder(context, SHORTCUT_ID)
            .setLocusId(LocusIdCompat(SHORTCUT_ID))
            .setShortLabel(context.getString(R.string.contact_name))
            .setIcon(cachedIcon)
            .setPerson(person)
            .setIntent(shortcutIntent)
            .setLongLived(true)
            .build()
        ShortcutManagerCompat.pushDynamicShortcut(context, shortcut)

        val target = Intent(context, BubbleActivity::class.java).apply {
            action = Intent.ACTION_VIEW
        }
        
        val bubbleIntent = PendingIntent.getActivity(
            context, 0, target,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val bubbleData = NotificationCompat.BubbleMetadata.Builder(bubbleIntent, cachedIcon)
            .setDesiredHeight(600)
            .setAutoExpandBubble(false) 
            .setSuppressNotification(false)
            .build()

        // DIRECT REPLY SETUP
        val replyPendingIntent = PendingIntent.getBroadcast(
            context,
            101,
            Intent(context, ReplyReceiver::class.java),
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val remoteInput = RemoteInput.Builder(KEY_TEXT_REPLY)
            .setLabel("Reply to Mezoinnit...")
            .build()

        val replyAction = NotificationCompat.Action.Builder(
            R.drawable.ic_send,
            "Reply",
            replyPendingIntent
        ).addRemoteInput(remoteInput).build()

        val style = NotificationCompat.MessagingStyle(person)
        style.conversationTitle = context.getString(R.string.contact_name)
        messages.takeLast(5).forEach { msg ->
            style.addMessage(msg.content, msg.timestamp, if (msg.isFromMe) null else person)
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_bubble)
            .addPerson(person)
            .setShortcutId(SHORTCUT_ID)
            .setLocusId(LocusIdCompat(SHORTCUT_ID))
            .setBubbleMetadata(bubbleData)
            .setContentIntent(bubbleIntent)
            .addAction(replyAction) // Integration of direct reply
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setPriority(NotificationCompat.PRIORITY_MAX) 
            .setDefaults(NotificationCompat.DEFAULT_ALL) 
            .setWhen(System.currentTimeMillis())
            .setShowWhen(true)
            .setStyle(style)
            .setAutoCancel(true)
            .setOnlyAlertOnce(false)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    fun areBubblesAllowed(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            notificationManager.areBubblesAllowed()
        } else {
            true
        }
    }

    fun openNotificationSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_BUBBLE_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                context.getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
                enableVibration(true)
                setShowBadge(true)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    setAllowBubbles(true)
                }
                description = context.getString(R.string.notification_channel_description)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }
}