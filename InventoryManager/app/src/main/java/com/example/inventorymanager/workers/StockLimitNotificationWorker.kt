package com.example.inventorymanager.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.inventorymanager.R
import com.example.inventorymanager.data.DefaultAppContainer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "StockLimitNotificationWorker"

// Existence Limit Notification Worker
class StockLimitNotificationWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            return@withContext try {

                val notificationMessage = DefaultAppContainer(applicationContext).inventoryRepository.getStockLimit()

                // If there are active notifications, send the notification
                if (notificationMessage.message != "") { //
                    makeNotification(
                        "Límite de existencias",
                        notificationMessage.message,
                        applicationContext,
                        "STOCK_LIMIT_NOTIFICATION",
                        1
                    )
                }

                Result.success()
            } catch (throwable: Throwable) {
                Log.e(
                    TAG,
                    applicationContext.resources.getString(R.string.error_sending_el_notification),
                    throwable
                )

                Result.failure()
            }
        }
    }
}