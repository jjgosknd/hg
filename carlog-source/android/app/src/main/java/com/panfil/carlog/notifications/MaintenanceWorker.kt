package com.panfil.carlog.notifications

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.panfil.carlog.CarLogApp
import com.panfil.carlog.MainActivity
import com.panfil.carlog.R
import com.panfil.carlog.data.db.MaintenanceDao
import com.panfil.carlog.data.prefs.PrefsStore
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class MaintenanceWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val maintenanceDao: MaintenanceDao,
    private val prefsStore: PrefsStore,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val carInfo = prefsStore.carInfo.first()
        val items = maintenanceDao.getAll().first()
        val currentMileage = carInfo.mileage
        val now = System.currentTimeMillis()

        items.filter { it.notifyEnabled }.forEach { m ->
            val mileageDue = m.lastMileage + m.mileageInterval
            val dateDue = m.lastDate + m.monthsInterval.toLong() * 30 * 24 * 60 * 60 * 1000

            val mileageSoon = m.mileageInterval > 0 && currentMileage >= mileageDue - 1000
            val dateSoon = m.monthsInterval > 0 && now >= dateDue - 7L * 24 * 60 * 60 * 1000

            if (mileageSoon || dateSoon) {
                sendNotification(m.id.toInt(), m.title, currentMileage, mileageDue)
            }
        }
        return Result.success()
    }

    private fun sendNotification(id: Int, title: String, current: Int, due: Int) {
        if (ContextCompat.checkSelfPermission(
                applicationContext, Manifest.permission.POST_NOTIFICATIONS,
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pending = PendingIntent.getActivity(
            applicationContext, id, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val text = if (due > 0) {
            "Пробег: $current км. Следующее ТО при $due км"
        } else {
            "Подходит срок техобслуживания"
        }

        val notification = NotificationCompat.Builder(applicationContext, CarLogApp.CHANNEL_MAINTENANCE)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("ТО: $title")
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pending)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(applicationContext).notify(id, notification)
    }
}
