package com.example.inventorymanager.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.inventorymanager.InventoryApplication
import com.example.inventorymanager.data.InventoryRepository
import com.example.inventorymanager.model.DetectionDTO
import com.example.inventorymanager.model.TableDetection
import com.example.inventorymanager.operations.DateTime
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import java.io.IOException

class ScanViewModel(
    private val inventoryRepository: InventoryRepository
) : ViewModel() {
    var scanState: ScanState by mutableStateOf(ScanState())
        private set

    fun getObjects() {
        viewModelScope.launch {
            scanState = scanState.copy(
                isLoading = true
            )

            scanState = try {
                val result = inventoryRepository.getObjects()

                val objectIDs = result.map {
                    it.objectID
                }

                scanState.copy(
                    objects = objectIDs,
                    errorGetting = false,
                    isLoading = false
                )
            } catch (e: IOException) {
                println("An error occurred while reading the database")
                scanState.copy(
                    isLoading = false,
                    errorGetting = true
                )
            } catch (e: HttpException) {
                println("An error occurred while reading the database")
                scanState.copy(
                    isLoading = false,
                    errorGetting = true
                )
            }
        }
    }

    fun getSpaces() {
        viewModelScope.launch {
            scanState = scanState.copy(
                isLoading = true
            )

            scanState = try {
                val result = inventoryRepository.getSpaces()

                val spaceIDs = result.map {
                    it.spaceID
                }

                scanState.copy(
                    spaces = spaceIDs,
                    spaceObjects = result,
                    errorGetting = false,
                    isLoading = false
                )
            } catch (e: IOException) {
                println("An error occurred while reading the database")
                scanState.copy(
                    isLoading = false,
                    errorGetting = true
                )
            } catch (e: HttpException) {
                println("An error occurred while reading the database")
                scanState.copy(
                    isLoading = false,
                    errorGetting = true
                )
            }
        }
    }

    // Check DetectionDTO´s Space
    @RequiresApi(Build.VERSION_CODES.O)
    fun onObjectDetected(detection: DetectionDTO) {
        viewModelScope.launch {
            runBlocking {
                val updatedDetections = scanState.detectedObjects.toMutableList()

                if (!containsObjectSc(updatedDetections, detection.objectID)) {
                    updatedDetections.add(detection)

                    scanState = scanState.copy(
                        detectedObjects = updatedDetections
                    )

                    // There is a space "selected"
                    if (scanState.currentSpace != -1) {
                        // Update the space in which the object was detected
                        detection.spaceID = scanState.currentSpace

                        try {
                            val result = inventoryRepository.getDetectionsBySpaceAndLastDate(
                                scanState.currentSpace.toString()
                            )

                            scanState = scanState.copy(
                                detections = result
                            )

                            val updatedInsertions = scanState.detections

                            val dateTime = DateTime()
                            val newCaptureDate = dateTime.normalizeDate(detection.captureDate)

                            if (updatedInsertions.isEmpty()) {
                                insertDetection(detection)

                            } else if (newCaptureDate.isAfter(dateTime.normalizeDate(updatedInsertions.last().captureDate))) {
                                insertDetection(detection)

                            } else if (newCaptureDate.isEqual(dateTime.normalizeDate(updatedInsertions.last().captureDate))) {
                                if (!containsObject(updatedInsertions, detection.objectID)) {
                                    insertDetection(detection)
                                }
                            }

                        } catch (e: IOException) {
                            println("An error occurred while reading the database")
                            scanState = scanState.copy(
                                errorGetting = true
                            )
                        } catch (e: HttpException) {
                            println("An error occurred while reading the database")
                            scanState = scanState.copy(
                                errorGetting = true
                            )
                        }
                    }
                }
            }
        }
    }

    fun containsObjectSc(
        scannedObjects: List<DetectionDTO>,
        objectID: Int
    ): Boolean {
        for (detection in scannedObjects) {
            if (detection.objectID == objectID) {
                return true
            }
        }

        return false
    }

    fun containsObject(
        detections: List<TableDetection>,
        objectID: Int
    ): Boolean {
        for (detection in detections) {
            if (detection.objectID == objectID) {
                return true
            }
        }

        return false
    }

    suspend fun insertDetection(d: DetectionDTO) {
        scanState = try {
            inventoryRepository.createDetection(d)

            val result = inventoryRepository.getDetectionsBySpaceAndLastDate(
                d.spaceID.toString()
            )

            scanState.copy(
                detections = result
            )

        } catch (e: IOException) {
            println("An error occurred while trying to insert, or reading the database")
            scanState.copy(
                errorInserting = true,
                errorGetting = true
            )
        } catch (e: HttpException) {
            println("An error occurred while trying to insert, or reading the database")
            scanState.copy(
                errorInserting = true,
                errorGetting = true
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onSpaceDetected(spaceID: Int) {
        viewModelScope.launch {
            runBlocking {
                if (spaceID != scanState.currentSpace) {
                    val previousSpace = scanState.currentSpace

                    // Selected Space
                    scanState = scanState.copy(currentSpace = spaceID)

                    // A space had not been selected, but objects had been detected
                    if (previousSpace == -1 && scanState.detectedObjects.isNotEmpty()) {
                        try {
                            val result = inventoryRepository.getDetectionsBySpaceAndLastDate(
                                spaceID.toString()
                            )

                            scanState = scanState.copy(
                                detections = result
                            )

                            val updatedInsertions = scanState.detections

                            val dateTime = DateTime()

                            for (element in scanState.detectedObjects) {
                                // Update the space in which the object was detected
                                element.spaceID = scanState.currentSpace

                                val newCaptureDate = dateTime.normalizeDate(element.captureDate)

                                if (updatedInsertions.isEmpty()) {
                                    insertDetection(element)

                                } else if (newCaptureDate.isAfter(dateTime.normalizeDate(updatedInsertions.last().captureDate))) {
                                    insertDetection(element)

                                } else if (newCaptureDate.isEqual(dateTime.normalizeDate(updatedInsertions.last().captureDate))) {
                                    if (!containsObject(updatedInsertions, element.objectID)) {
                                        insertDetection(element)
                                    }
                                }
                            }

                        } catch (e: IOException) {
                            println("An error occurred while reading the database")
                            scanState = scanState.copy(
                                errorGetting = true
                            )
                        } catch (e: HttpException) {
                            println("An error occurred while reading the database")
                            scanState = scanState.copy(
                                errorGetting = true
                            )
                        }
                    } else if (previousSpace != -1) {
                        // A space had already been selected, therefore the detections were already
                        // attempted to be stored in the database. A new space has been selected and
                        // the detections must be emptied into the previous space.
                        scanState = scanState.copy(detectedObjects = listOf())
                    }
                }
            }
        }
    }

    fun setBoundingBox(rect: Rect?) {
        scanState = scanState.copy(boundingBox = rect)
    }
    fun setScannedSpaceQRCode(spaceQR: String?) {
        scanState = scanState.copy(scannedSpaceQRCode = spaceQR)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as InventoryApplication)
                val inventoryRepository = application.container.inventoryRepository
                ScanViewModel(inventoryRepository = inventoryRepository)
            }
        }
    }
}