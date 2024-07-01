package com.example.inventorymanager.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.inventorymanager.InventoryApplication
import com.example.inventorymanager.data.InventoryRepository
import com.example.inventorymanager.model.ModelObject
import com.example.inventorymanager.model.Notification
import com.example.inventorymanager.model.NotificationActive
import com.example.inventorymanager.model.NotificationDTO
import com.example.inventorymanager.model.NotificationStockLimit
import com.example.inventorymanager.model.Space
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class NotificationViewModel(
    private val inventoryRepository: InventoryRepository
) : ViewModel() {
    var notificationState: NotificationState by mutableStateOf(NotificationState())
        private  set

    fun getNotifications() {
        viewModelScope.launch {
            notificationState = notificationState.copy(
                isLoading = true
            )

            notificationState = try {
                val result = inventoryRepository.getNotifications()

                notificationState.copy(
                    notifications = result,
                    error = false,
                    isLoading = false
                )
            } catch (e: IOException) {
                println("An error occurred while reading the database")
                notificationState.copy(
                    isLoading = false,
                    error = true
                )
            } catch (e: HttpException) {
                println("An error occurred while reading the database")
                notificationState.copy(
                    isLoading = false,
                    error = true
                )
            }
        }
    }

    fun getObjects() {
        viewModelScope.launch {
            notificationState = notificationState.copy(
                isLoading = true
            )

            notificationState = try {
                val result = inventoryRepository.getObjects()

                notificationState.copy(
                    objects = result,
                    error = false,
                    isLoading = false
                )
            } catch (e: IOException) {
                println("An error occurred while reading the database")
                notificationState.copy(
                    isLoading = false,
                    error = true
                )
            } catch (e: HttpException) {
                println("An error occurred while reading the database")
                notificationState.copy(
                    isLoading = false,
                    error = true
                )
            }
        }
    }

    fun getSpaces() {
        viewModelScope.launch {
            notificationState = notificationState.copy(
                isLoading = true
            )

            notificationState = try {
                val result = inventoryRepository.getSpaces()

                notificationState.copy(
                    spaces = result,
                    error = false,
                    isLoading = false
                )
            } catch (e: IOException) {
                println("An error occurred while reading the database")
                notificationState.copy(
                    isLoading = false,
                    error = true
                )
            } catch (e: HttpException) {
                println("An error occurred while reading the database")
                notificationState.copy(
                    isLoading = false,
                    error = true
                )
            }
        }
    }

    fun deleteNotification(id: String) {
        viewModelScope.launch {
            notificationState = try {
                inventoryRepository.deleteNotification(id)

                val notificationList = inventoryRepository.getNotifications()

                notificationState.copy(
                    notifications = notificationList
                )

            } catch (e: IOException) {
                println("An error occurred while deleting")
                println(e.message)
                notificationState.copy(
                    error = true
                )
            } catch (e: HttpException) {
                println("An error occurred while deleting")
                println(e.response())
                notificationState.copy(
                    error = true
                )
            }
        }
    }

    fun onObjectClicked(modelObject: ModelObject) {
        notificationState = notificationState.copy(
            objectIDSelected = modelObject.objectID
        )
    }

    fun onSpaceClicked(space: Space) {
        notificationState = notificationState.copy(
            spaceIDSelected = space.spaceID
        )
    }

    fun setNotificationType(type: Int) {
        notificationState = notificationState.copy(
            notificationTypeSelected = type
        )
    }

    fun setStockLimit(limit: String) {
        notificationState = notificationState.copy(
            stockLimitSelected = limit
        )
    }

    fun setNotificationRowSelected(value: Int) {
        notificationState = notificationState.copy(
            notificationRowSelected = value
        )
    }

    fun setNotificationPeriodType(value: Int) {
        notificationState = notificationState.copy(
            notificationPeriodType = value
        )
    }

    fun setNotificationPeriod(value: String) {
        notificationState = notificationState.copy(
            notificationPeriod = value
        )
    }

    fun postNotification(notification: NotificationDTO) {
        viewModelScope.launch {
            notificationState = notificationState.copy(
                isLoading = true
            )

            notificationState = try {
                inventoryRepository.createNotification(notification)

                val notifications = inventoryRepository.getNotifications()

                notificationState.copy(
                    notifications = notifications,
                    error = false,
                    isLoading = false
                )

            } catch (e: IOException) {
                println("An error occurred while creating")
                notificationState.copy(
                    error = true,
                    isLoading = false
                )
            } catch (e: HttpException) {
                println("An error occurred while creating")
                notificationState.copy(
                    error = true,
                    isLoading = false
                )
            }
        }
    }

    fun onSwitchChanged(
        notification: Notification,
        value: Int, previousNotifications: List<Notification>
    ) {
        viewModelScope.launch {
            val notificationActive = NotificationActive(value)

            notificationState = try {
                inventoryRepository.updateActive(
                    notificationActive,
                    notification.notificationID.toString()
                )

                notificationState.copy(
                    error = false
                )
            } catch (e: IOException) {
                println("An error occurred while updating the database")
                notificationState.copy(
                    error = true,
                    // Reverts updates made in the Switch's onCheckedChange
                    notifications = previousNotifications
                )
            } catch (e: HttpException) {
                println("An error occurred while updating the database")
                notificationState.copy(
                    error = true,
                    // Reverts updates made in the Switch's onCheckedChange
                    notifications = previousNotifications
                )
            }
        }
    }

    fun updateStockLimit() {
        viewModelScope.launch {
            notificationState = try {
                val notification = NotificationStockLimit(
                    notificationState.stockLimitSelected.toInt()
                )

                inventoryRepository.updateStockLimit(
                    notification,
                    notificationState.notificationRowSelected.toString()
                )
                val notificationList = inventoryRepository.getNotifications()

                notificationState.copy(
                    notifications = notificationList,
                    error = false
                )
            } catch (e: IOException) {
                println("An error occurred while updating the database")
                e.printStackTrace()
                notificationState.copy(
                    error = true
                )
            } catch (e: HttpException) {
                println("An error occurred while updating the database")
                e.printStackTrace()
                notificationState.copy(
                    error = true
                )
            }
        }
    }

    fun setNotifications(value: List<Notification>) {
        notificationState = notificationState.copy(
            notifications = value
        )
    }

    fun sendStockLimitNotification(interval: Long) {
        inventoryRepository.sendStockLimitNotification(interval)
    }

    fun sendAbsenceNotification(interval: Long) {
        inventoryRepository.sendAbsenceNotification(interval)
    }

    init {
        getNotifications()
        getObjects()
        getSpaces()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as InventoryApplication)
                val inventoryRepository = application.container.inventoryRepository
                NotificationViewModel(inventoryRepository = inventoryRepository)
            }
        }
    }
}