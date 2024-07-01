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
import com.example.inventorymanager.model.ModelObjectDTO
import com.example.inventorymanager.model.ObjectSpaceFO
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ObjectViewModel(
    private val inventoryRepository: InventoryRepository
) : ViewModel() {
    var objectState: ObjectState by mutableStateOf(ObjectState())
        private set

    fun getObjects() {
        viewModelScope.launch {
            objectState = objectState.copy(
                isLoading = true
            )

            objectState = try {
                val result = inventoryRepository.getObjects()

                objectState.copy(
                    objects = result,
                    error = false,
                    isLoading = false
                )
            } catch (e: IOException) {
                println("An error occurred while reading the database")
                objectState.copy(
                    isLoading = false,
                    error = true
                )
            } catch (e: HttpException) {
                println("An error occurred while reading the database")
                objectState.copy(
                    isLoading = false,
                    error = true
                )
            }
        }
    }

    fun addObject(modelObject: ModelObjectDTO) {
        viewModelScope.launch {
            objectState = try {
                val result = inventoryRepository.createObject(modelObject)

                // An ObjectSpace is added from an Object
                inventoryRepository.createOSFromObject(ObjectSpaceFO(result.objectID))

                val objectsList = objectState.objects.toMutableList()

                objectsList.add(result)

                objectState.copy(
                    objects = objectsList,
                    objectNewName = "",
                    objectNewPrice = ""
                )

            } catch (e: IOException) {
                println("An error occurred while creating")
                objectState.copy(
                    objectNewName = "",
                    objectNewPrice = "",
                    error = true
                )
            } catch (e: HttpException) {
                println("An error occurred while creating")
                objectState.copy(
                    objectNewName = "",
                    objectNewPrice = "",
                    error = true
                )
            }
        }
    }

    fun updateObject(modelObject: ModelObjectDTO, id: String) {
        viewModelScope.launch {
            objectState = try {
                val result = inventoryRepository.updateObject(modelObject, id)

                val objectsList = objectState.objects.toMutableList()

                for (i in objectsList.indices) {
                    if (objectsList[i].objectID == result.objectID) {
                        objectsList[i] = result
                        break
                    }
                }

                objectState.copy(
                    objects = objectsList,
                    objectRowClicked = result
                )

            } catch (e: IOException) {
                println("An error occurred while updating")
                objectState.copy(
                    error = true
                )
            } catch (e: HttpException) {
                println("An error occurred while updating")
                objectState.copy(
                    error = true
                )
            }
        }
    }

    fun deleteObject(id: String) {
        viewModelScope.launch {
            objectState = try {
                inventoryRepository.deleteObject(id)

                val objectsList = objectState.objects.toMutableList()

                for (i in objectsList.indices) {
                    if (objectsList[i].objectID == id.toInt()) {
                        objectsList.removeAt(i)
                        break
                    }
                }

                objectState.copy(
                    objects = objectsList,
                    objectRowClicked = ModelObject(0, "", 0.0F, "")
                )

            } catch (e: IOException) {
                println("An error occurred while deleting")
                println(e.message)
                objectState.copy(
                    error = true
                )
            } catch (e: HttpException) {
                println("An error occurred while deleting")
                println(e.response())
                objectState.copy(
                    error = true
                )
            }
        }
    }

    fun onObjectClicked(objectID: String) {
        viewModelScope.launch {
            objectState = objectState.copy(
                isLoading = true
            )

            objectState = try {
                val result = inventoryRepository.getObject(objectID)

                objectState.copy(
                    objectRowClicked = result,
                    error = false,
                    isLoading = false
                )
            } catch (e: IOException) {
                println("An error occurred while reading the database")
                objectState.copy(
                    isLoading = false,
                    error = true
                )
            } catch (e: HttpException) {
                println("An error occurred while reading the database")
                objectState.copy(
                    isLoading = false,
                    error = true
                )
            }
        }
    }

    fun onEditObject(objectQR: ModelObject) {
        objectState = objectState.copy(
            objectEdit = objectQR,
            objectEditName = objectQR.objectName,
            objectEditPrice = objectQR.objectPrice.toString()
        )
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as InventoryApplication)
                val inventoryRepository = application.container.inventoryRepository
                ObjectViewModel(inventoryRepository = inventoryRepository)
            }
        }
    }

    fun changeObjectName(name: String) {
        objectState = objectState.copy(objectEditName = name)
    }

    fun changeObjectPrice(price: String) {
        objectState = objectState.copy(objectEditPrice = price)
    }

    fun changeNewObjectName(name: String) {
        objectState = objectState.copy(objectNewName = name)
    }

    fun changeNewObjectPrice(price: String) {
        objectState = objectState.copy(objectNewPrice = price)
    }

    fun recordSingleCapture(id: String) {
        onObjectClicked(id)
    }

    init {
        getObjects()
    }
}