package com.example.inventorymanager.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.example.inventorymanager.model.Space
import com.example.inventorymanager.operations.DateTime
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class DetectionViewModel(
    private val inventoryRepository: InventoryRepository
) : ViewModel() {
    var detectionState: DetectionState by mutableStateOf(DetectionState())
        private  set

    fun getSpaces() {
        viewModelScope.launch {
            detectionState = detectionState.copy(
                isLoading = true
            )

            detectionState = try {
                val result = inventoryRepository.getSpaces()

                detectionState.copy(
                    spaces = result,
                    error = false,
                    isLoading = false
                )
            } catch (e: IOException) {
                println("An error occurred while reading the database")
                detectionState.copy(
                    isLoading = false,
                    error = true
                )
            } catch (e: HttpException) {
                println("An error occurred while reading the database")
                detectionState.copy(
                    isLoading = false,
                    error = true
                )
            }
        }
    }

    fun getObjects() {
        viewModelScope.launch {
            detectionState = detectionState.copy(
                isLoading = true
            )

            detectionState = try {
                val result = inventoryRepository.getObjects()

                detectionState.copy(
                    databaseObjects = result,
                    error = false,
                    isLoading = false
                )
            } catch (e: IOException) {
                println("An error occurred while reading the database")
                detectionState.copy(
                    isLoading = false,
                    error = true
                )
            } catch (e: HttpException) {
                println("An error occurred while reading the database")
                detectionState.copy(
                    isLoading = false,
                    error = true
                )
            }
        }
    }

    fun getObjectsSpaces(spaceID: String) {
        viewModelScope.launch {
            detectionState = detectionState.copy(
                isLoading = true
            )

            detectionState = try {
                val result = inventoryRepository.getObjectsSpacesBySpace(spaceID)

                detectionState.copy(
                    objectsSpacesBySpace = result,
                    error = false,
                    isLoading = false
                )
            } catch (e: IOException) {
                println("An error occurred while reading the database")
                detectionState.copy(
                    isLoading = false,
                    error = true
                )
            } catch (e: HttpException) {
                println("An error occurred while reading the database")
                detectionState.copy(
                    isLoading = false,
                    error = true
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDetections(spaceID: String) {
        viewModelScope.launch {
            detectionState = detectionState.copy(
                isLoading = true
            )

            detectionState = try {
                val result = inventoryRepository.getDetectionsBySpaceAndLastDate(spaceID)

                var date = ""

                if (result.isNotEmpty()) {
                    val dateTime = DateTime()
                    date = dateTime.normalizeDate(result.last().captureDate).toString()
                }

                detectionState.copy(
                    lastDetectionsBySpace = result,
                    lastDetectionsDate = date,
                    error = false,
                    isLoading = false
                )
            } catch (e: IOException) {
                println("An error occurred while reading the database")
                detectionState.copy(
                    isLoading = false,
                    error = true
                )
            } catch (e: HttpException) {
                println("An error occurred while reading the database")
                detectionState.copy(
                    isLoading = false,
                    error = true
                )
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as InventoryApplication)
                val inventoryRepository = application.container.inventoryRepository
                DetectionViewModel(inventoryRepository = inventoryRepository)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onSpaceClicked(space: Space) {
        viewModelScope.launch {
            detectionState = detectionState.copy(spaceRowClicked = space)

            getObjectsSpaces(detectionState.spaceRowClicked.spaceID.toString())
            getDetections(detectionState.spaceRowClicked.spaceID.toString())
        }
    }

    fun getMissingObjects() {
        viewModelScope.launch {
            val allObjects = detectionState.objectsSpacesBySpace.map { it.objectID }

            val foundObjects = detectionState.lastDetectionsBySpace.map { it.objectID }

            val missingObjects = allObjects.subtract(foundObjects.toSet())
            val missingObjectsList: MutableList<ModelObject> = mutableListOf()

            val allRegisteredObjectsIDs = detectionState.databaseObjects.map { it.objectID }

            missingObjects.forEach { objectID ->

                // We retrieve information locally
                if (allRegisteredObjectsIDs.contains(objectID)) {
                    val i = allRegisteredObjectsIDs.indexOf(objectID)
                    missingObjectsList.add(detectionState.databaseObjects.get(i))
                } else {
                    // We bring the information from the DB
                    detectionState = detectionState.copy(
                        isLoading = true
                    )

                    detectionState = try {
                        missingObjectsList.add(inventoryRepository.getObject(objectID.toString()))

                        detectionState.copy(
                            error = false,
                            isLoading = false
                        )
                    } catch (e: IOException) {
                        println("An error occurred while reading the database")
                        detectionState.copy(
                            isLoading = false,
                            error = true
                        )
                    } catch (e: HttpException) {
                        println("An error occurred while reading the database")
                        detectionState.copy(
                            isLoading = false,
                            error = true
                        )
                    }

                }
            }

            detectionState = detectionState.copy(
                missingObjectsBySpace = missingObjectsList
            )
        }
    }

    fun onObjectClicked(objectClicked: ModelObject) {
        detectionState = detectionState.copy(
            objectClicked = objectClicked
        )
    }

    fun setLastDetectionsDate(value: String) {
        viewModelScope.launch {
            detectionState = detectionState.copy(
                lastDetectionsDate = value
            )
        }
    }
}