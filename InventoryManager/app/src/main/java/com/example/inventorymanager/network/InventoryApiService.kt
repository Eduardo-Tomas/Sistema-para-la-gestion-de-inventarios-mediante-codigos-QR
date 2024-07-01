package com.example.inventorymanager.network

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
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface InventoryApiService {
    // Objects
    @POST("objects")
    suspend fun createObject(
        @Body modelObject: ModelObjectDTO
    ): ModelObject

    @GET("objects")
    suspend fun getObjects(): List<ModelObject>

    @GET("objects/{id}")
    suspend fun getObject(
        @Path("id") id: String
    ): ModelObject

    @PATCH("objects/{id}")
    suspend fun updateObject(
        @Body modelObject: ModelObjectDTO,
        @Path("id") id: String
    ): ModelObject

    @DELETE("objects/{id}")
    suspend fun deleteObject(
        @Path("id") id: String
    )

    // Spaces
    @POST("spaces")
    suspend fun createSpace(
        @Body space: SpaceDTO
    ): Space

    @GET("spaces")
    suspend fun getSpaces(): List<Space>

    @GET("spaces/{id}")
    suspend fun getSpace(
        @Path("id") id: String
    ): Space

    @PATCH("spaces/{id}")
    suspend fun updateSpace(
        @Body space: SpaceDTO,
        @Path("id") id: String
    ): Space

    @DELETE("spaces/{id}")
    suspend fun deleteSpace(
        @Path("id") id: String
    )

    // ObjectsSpaces
    @POST("objectsSpacesFO")
    suspend fun createOSFromObject(
        @Body objectSpace: ObjectSpaceFO
    )

    @POST("objectsSpacesFS")
    suspend fun createOSFromSpace(
        @Body objectSpace: ObjectSpaceFS
    )

    @GET("objectsSpaces")
    suspend fun getObjectsSpaces(): List<ObjectSpace>

    @GET("objectsSpaces/space/{id}")
    suspend fun getObjectsSpacesBySpace(
        @Path("id") id: String
    ): List<ObjectSpace>

    // Detections
    @POST("detections")
    suspend fun createDetection(
        @Body detection: DetectionDTO
    ): Detection

    @GET("detections")
    suspend fun getDetections(): List<TableDetection>

    @GET("detections/last")
    suspend fun getDetectionsByLastDate(): List<TableDetection>

    @GET("detections/last/{id}")
    suspend fun getDetectionsBySpaceAndLastDate(
        @Path("id") id: String
    ): List<TableDetection>

    // Notifications
    @POST("notifications")
    suspend fun createNotification(
        @Body notification: NotificationDTO
    ): NotificationC

    @GET("notifications")
    suspend fun getNotifications(): List<Notification>

    @GET("notifications/stockLimit")
    suspend fun getStockLimit(): NotificationMessage

    @GET("notifications/absence")
    suspend fun getAbsence(): NotificationMessage

    @GET("notifications/{id}")
    suspend fun getNotification(
        @Path("id") id: String
    ): Notification

    @PATCH("notifications/{id}")
    suspend fun updateNotification(
        @Body notification: NotificationDTO,
        @Path("id") id: String
    ): NotificationC

    @PATCH("notifications/{id}")
    suspend fun updateActive(
        @Body notification: NotificationActive,
        @Path("id") id: String
    ): NotificationC

    @PATCH("notifications/{id}")
    suspend fun updateStockLimit(
        @Body notification: NotificationStockLimit,
        @Path("id") id: String
    ): NotificationC

    @DELETE("notifications/{id}")
    suspend fun deleteNotification(
        @Path("id") id: String
    )

    // PDFs
    @PATCH("reports/spacesReport.pdf")
    suspend fun getSpacesReport(
        @Body pdf: PDF
    ): ResponseBody

    @PATCH("reports/detailedReportBySpace.pdf")
    suspend fun getDetailedReport(
        @Body pdf: PDF
    ): ResponseBody
}