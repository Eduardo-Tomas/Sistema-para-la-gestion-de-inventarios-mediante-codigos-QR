package com.example.inventorymanager.data

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.inventorymanager.model.Detection
import com.example.inventorymanager.model.DetectionDTO
import com.example.inventorymanager.model.ModelObject
import com.example.inventorymanager.model.ModelObjectDTO
import com.example.inventorymanager.model.Notification
import com.example.inventorymanager.model.NotificationActive
import com.example.inventorymanager.model.NotificationC
import com.example.inventorymanager.model.NotificationDTO
import com.example.inventorymanager.model.NotificationMessage
import com.example.inventorymanager.model.NotificationStockLimit
import com.example.inventorymanager.model.ObjectSpace
import com.example.inventorymanager.model.ObjectSpaceFO
import com.example.inventorymanager.model.ObjectSpaceFS
import com.example.inventorymanager.model.PDF
import com.example.inventorymanager.model.Space
import com.example.inventorymanager.model.SpaceDTO
import com.example.inventorymanager.model.TableDetection
import com.example.inventorymanager.network.InventoryApiService
import com.example.inventorymanager.workers.AbsenceNotificationWorker
import com.example.inventorymanager.workers.StockLimitNotificationWorker
import okhttp3.ResponseBody
import java.util.Calendar
import java.util.concurrent.TimeUnit

interface InventoryRepository {
    // Objects
    suspend fun createObject(
        modelObject: ModelObjectDTO
    ): ModelObject

    suspend fun getObjects(): List<ModelObject>

    suspend fun getObject(
        id: String
    ): ModelObject

    suspend fun updateObject(
        modelObject: ModelObjectDTO,
        id: String
    ): ModelObject

    suspend fun deleteObject(id: String)

    // Spaces
    suspend fun createSpace(
        space: SpaceDTO
    ): Space

    suspend fun getSpaces(): List<Space>

    suspend fun getSpace(
        id: String
    ): Space

    suspend fun updateSpace(
        space: SpaceDTO,
        id: String
    ): Space

    suspend fun deleteSpace(
        id: String
    )

    // ObjectsSpaces
    suspend fun createOSFromObject(
        objectSpace: ObjectSpaceFO
    )

    suspend fun createOSFromSpace(
        objectSpace: ObjectSpaceFS
    )

    suspend fun getObjectsSpaces(): List<ObjectSpace>

    suspend fun getObjectsSpacesBySpace(
        id: String
    ): List<ObjectSpace>

    // Detections
    suspend fun createDetection(
        detection: DetectionDTO
    ): Detection

    suspend fun getDetections(): List<TableDetection>

    suspend fun getDetectionsByLastDate(): List<TableDetection>

    suspend fun getDetectionsBySpaceAndLastDate(
        id: String
    ): List<TableDetection>

    // Notifications
    suspend fun createNotification(
        notification: NotificationDTO
    ): NotificationC

    suspend fun getNotifications(): List<Notification>

    suspend fun getStockLimit(): NotificationMessage

    suspend fun getAbsence(): NotificationMessage

    suspend fun getNotification(
        id: String
    ): Notification

    suspend fun updateNotification(
        notification: NotificationDTO,
        id: String
    ): NotificationC

    suspend fun updateActive(
        notification: NotificationActive,
        id: String
    ): NotificationC

    suspend fun updateStockLimit(
        notification: NotificationStockLimit,
        id: String
    ): NotificationC

    suspend fun deleteNotification(
        id: String
    )

    fun sendStockLimitNotification(interval: Long)

    fun sendAbsenceNotification(interval: Long)

    // PDF
    suspend fun getSpacesReport(
        pdf: PDF
    ): ResponseBody

    suspend fun getDetailedReport(
        pdf: PDF
    ): ResponseBody
}

class NetworkInventoryRepository(
    private val inventoryApiService: InventoryApiService,
    context: Context
) : InventoryRepository {

    // Objects
    override suspend fun createObject(
        modelObject: ModelObjectDTO
    ): ModelObject = inventoryApiService.createObject(modelObject)

    override suspend fun getObjects(): List<ModelObject> =
        inventoryApiService.getObjects()

    override suspend fun getObject(
        id: String
    ): ModelObject = inventoryApiService.getObject(id)

    override suspend fun updateObject(
        modelObject: ModelObjectDTO,
        id: String
    ): ModelObject =
        inventoryApiService.updateObject(modelObject, id)

    override suspend fun deleteObject(
        id: String
    ) = inventoryApiService.deleteObject(id)

    // Spaces
    override suspend fun createSpace(
        space: SpaceDTO
    ): Space = inventoryApiService.createSpace(space)

    override suspend fun getSpaces(): List<Space> =
        inventoryApiService.getSpaces()

    override suspend fun getSpace(
        id: String
    ): Space = inventoryApiService.getSpace(id)

    override suspend fun updateSpace(
        space: SpaceDTO,
        id: String
    ): Space = inventoryApiService.updateSpace(space, id)

    override suspend fun deleteSpace(
        id: String
    ) = inventoryApiService.deleteSpace(id)

    // ObjectsSpaces
    override suspend fun createOSFromObject(
        objectSpace: ObjectSpaceFO
    ) = inventoryApiService.createOSFromObject(objectSpace)

    override suspend fun createOSFromSpace(
        objectSpace: ObjectSpaceFS
    ) = inventoryApiService.createOSFromSpace(objectSpace)

    override suspend fun getObjectsSpaces(): List<ObjectSpace> =
        inventoryApiService.getObjectsSpaces()

    override suspend fun getObjectsSpacesBySpace(
        id: String
    ): List<ObjectSpace> =
        inventoryApiService.getObjectsSpacesBySpace(id)

    // Detections
    override suspend fun createDetection(
        detection: DetectionDTO
    ): Detection = inventoryApiService.createDetection(detection)

    override suspend fun getDetections(): List<TableDetection> =
        inventoryApiService.getDetections()

    override suspend fun getDetectionsByLastDate(): List<TableDetection> =
        inventoryApiService.getDetectionsByLastDate()

    override suspend fun getDetectionsBySpaceAndLastDate(
        id: String
    ): List<TableDetection> =
        inventoryApiService.getDetectionsBySpaceAndLastDate(id)

    // Notifications
    override suspend fun createNotification(
        notification: NotificationDTO
    ): NotificationC =
        inventoryApiService.createNotification(notification)

    override suspend fun getNotifications(): List<Notification> =
        inventoryApiService.getNotifications()

    override suspend fun getStockLimit(): NotificationMessage =
        inventoryApiService.getStockLimit()

    override suspend fun getAbsence(): NotificationMessage =
        inventoryApiService.getAbsence()

    override suspend fun getNotification(
        id: String
    ): Notification = inventoryApiService.getNotification(id)

    override suspend fun updateNotification(
        notification: NotificationDTO,
        id: String
    ): NotificationC =
        inventoryApiService.updateNotification(notification, id)

    override suspend fun updateActive(
        notification: NotificationActive,
        id: String
    ): NotificationC =
        inventoryApiService.updateActive(notification, id)

    override suspend fun updateStockLimit(
        notification: NotificationStockLimit,
        id: String
    ): NotificationC =
        inventoryApiService.updateStockLimit(notification, id)

    override suspend fun deleteNotification(
        id: String
    ) = inventoryApiService.deleteNotification(id)

    private val workManager = WorkManager.getInstance(context)
    
    override fun sendStockLimitNotification(interval: Long) {
        println(interval)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // Create a periodic work request
        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<StockLimitNotificationWorker>(
                interval,
                TimeUnit.DAYS
            )
            /*.setInitialDelay(calculateMonthlyDelay(), TimeUnit.MILLISECONDS)*/
            .setConstraints(constraints)
            .build()

        // Schedule the periodic job
        workManager.enqueueUniquePeriodicWork(
            "periodic_notification_work",
            /*ExistingPeriodicWorkPolicy.KEEP,*/
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            periodicWorkRequest
        )
    }

    override fun sendAbsenceNotification(interval: Long) {
        println(interval)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<AbsenceNotificationWorker>(
                interval,
                TimeUnit.DAYS
            )
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "periodic_notification",
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            periodicWorkRequest
        )
    }

    // PDF
    override suspend fun getSpacesReport(
        pdf: PDF
    ): ResponseBody = inventoryApiService.getSpacesReport(pdf)

    override suspend fun getDetailedReport(
        pdf: PDF
    ): ResponseBody = inventoryApiService.getDetailedReport(pdf)
}

// Every first day of the month
fun calculateMonthlyDelay(): Long {
    val now = Calendar.getInstance()

    val target = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 8)
        set(Calendar.MINUTE, 30)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    // If the target date is before the current date, it means we are
    // already past 8:30 AM. on the first day of the month, so a month
    // is added to the target date.
    if (target.before(now)) {
        target.add(Calendar.MONTH, 1)
    }

    return target.timeInMillis - now.timeInMillis
}

// Every monday at 8:00 a.m.
fun calculateMondayDelay(): Long {
    val now = Calendar.getInstance()

    val calendar = Calendar.getInstance()

    val isMonday = calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
    val beforeEight = calendar.get(Calendar.HOUR_OF_DAY) < 8

    if (!(isMonday && beforeEight)) {
        // It is scheduled for 8 am the following Monday
        do {
            calendar.add(Calendar.DATE, 1)
        } while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY)
    }

    // It is scheduled for 8 am
    calendar.set(Calendar.HOUR_OF_DAY, 8)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    return calendar.timeInMillis - now.timeInMillis
}