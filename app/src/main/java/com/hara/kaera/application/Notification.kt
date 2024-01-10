package com.hara.kaera.application

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.hara.kaera.R
import com.hara.kaera.domain.entity.WorryDetailEntity
import com.hara.kaera.feature.MainActivity
import com.hara.kaera.feature.detail.DetailBeforeActivity
import timber.log.Timber


class Notification(
    private val context: Context,
    private val title: String,
    private val body: String,
    private val data: String?
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
//        val resultPendingIntent: PendingIntent = if (data != null) {
//            setDetailActivityPendingIntent(data)
//        } else {
//            setHomePendingIntent()
//        }
// forground상태에서는 우선 아무런 액션을 취하지 않도록 둔다
        val resultPendingIntent = setHomePendingIntent()
        return with(context) {
            NotificationCompat.Builder(this, getString(R.string.project_id))
                .setSmallIcon(R.drawable.typography_kaera)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(body) // 줄넘김을 위해서 확장 알림으로 설정
                )
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(resultPendingIntent)
        }
    }

    /*
    앱이 foreground 상태에만 동작, background에서는 자동으로 런처 화면으로 이동
     */
    private fun setHomePendingIntent(): PendingIntent {
        Timber.e("main")
        val resultIntent = Intent()
        //val resultIntent = Intent(context, MainActivity::class.java)

        return TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }
    }

    private fun setDetailActivityPendingIntent(worryId: String): PendingIntent {
        Timber.e("detail")
        val resultIntent = Intent(context, DetailBeforeActivity::class.java).apply {
            putExtra("action", "view")
            putExtra(
                "worryDetail", WorryDetailEntity(
                    worryId = worryId.toInt(),
                    title = "",
                    templateId = 0,
                    subtitles = emptyList(),
                    answers = emptyList(),
                    period = "",
                    updatedAt = "",
                    deadline = "",
                    dDay = -1,
                    finalAnswer = "",
                    review = WorryDetailEntity.Review(
                        content = "",
                        updatedAt = ""
                    )
                )
            )
        }
        return TaskStackBuilder.create(context).run {
            addParentStack(MainActivity::class.java)
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }
    }

    private fun createNotificationChannel() {
        val name = context.getString(R.string.project_id)
        val descriptionText = "Description"
        val importance = NotificationManager.IMPORTANCE_MAX
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