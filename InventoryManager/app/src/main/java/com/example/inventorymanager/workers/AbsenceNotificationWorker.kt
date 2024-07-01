package com.example.inventorymanager.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.inventorymanager.R
import com.example.inventorymanager.data.DefaultAppContainer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

private const val TAG = "AbsenceNotificationWorker"

class AbsenceNotificationWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            return@withContext try {

                val notificationMessage = DefaultAppContainer(applicationContext).inventoryRepository.getAbsence()

                // If there are active notifications, send the notification
                if (notificationMessage.message != "") { //
                    makeNotification(
                        "Ausencia",
                        notificationMessage.message,
                        applicationContext,
                        "ABSENCE_NOTIFICATION",
                        2
                    )
                }

                Result.success()
            } catch (throwable: Throwable) {
                Log.e(
                    TAG,
                    applicationContext.resources.getString(R.string.error_sending_a_notification),
                    throwable
                )

                Result.failure()
            }
        }
    }
}