package com.hara.kaera.application

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.hara.kaera.R


class Notification(
    private val context: Context,
    private val title: String,
    private val body: String
) {
    init {
        with(context) {
            val builder = setBuilder()
            createNotificationChannel()

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(1, builder.build())
        }
    }

    private fun setBuilder(): NotificationCompat.Builder {
        //val resultPendingIntent = setPendingIntent()

        return with(context) {
            NotificationCompat.Builder(this, getString(R.string.project_id))
                .setSmallIcon(R.drawable.gem_blue_l)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(body) // 줄넘김을 위해서 확장 알림으로 설정
                )
                .setPriority(NotificationCompat.PRIORITY_MAX)
                //.setFullScreenIntent(resultPendingIntent , true)
        }
    }

//    private fun setPendingIntent(): PendingIntent {
//        //TODO HOME으로 갈지 Splash로 갈지 고민 Splash가 가장 깔끔하다고 생각
//        // val resultIntent = Intent(context, SplashActivity::class.java)
//
//        return TaskStackBuilder.create(context).run {
//            addNextIntentWithParentStack(resultIntent)
//            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
//        }
//    }

    private fun createNotificationChannel() {
        val name = context.getString(R.string.project_id)
        val descriptionText = "Description"
        val importance = NotificationManager.IMPORTANCE_UNSPECIFIED
        val channel = NotificationChannel(
            context.getString(R.string.project_id),
            name,
            importance
        ).apply {
            description = descriptionText
            setShowBadge(true)
            enableVibration(true)
            enableLights(true)
            lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }

}