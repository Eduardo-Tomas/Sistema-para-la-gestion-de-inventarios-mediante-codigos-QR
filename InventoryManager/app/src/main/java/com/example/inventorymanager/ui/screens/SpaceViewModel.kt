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
import com.example.inventorymanager.model.ObjectSpaceFS
import com.example.inventorymanager.model.Space
import com.example.inventorymanager.model.SpaceDTO
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class SpaceViewModel(
    private val inventoryRepository: InventoryRepository
) : ViewModel() {
    var spaceState: SpaceState by mutableStateOf(SpaceState())
        private  set

    fun getSpaces() {
        viewModelScope.launch {
            spaceState = spaceState.copy(
                isLoading = true
            )

            spaceState = try {
                val result = inventoryRepository.getSpaces()

                spaceState.copy(
                    spaces = result,
                    error = false,
                    isLoading = false
                )
            } catch (e: IOException) {
                println("An error occurred while reading the database")
                spaceState.copy(
                    isLoading = false,
                    error = true
                )
            } catch (e: HttpException) {
                println("An error occurred while reading the database")
                spaceState.copy(
                    isLoading = false,
                    error = true
                )
            }
        }
    }

    fun addSpace(space: SpaceDTO) {
        viewModelScope.launch {
            spaceState = try {
                val result = inventoryRepository.createSpace(space)

                // An ObjectSpace is added from an Space
                inventoryRepository.createOSFromSpace(
                    ObjectSpaceFS(result.spaceID)
                )

                val spacesList = spaceState.spaces.toMutableList()

                spacesList.add(result)

                spaceState.copy(
                    spaces = spacesList,
                    spaceNewName = "",
                )

            } catch (e: IOException) {
                println("An error occurred while creating")
                println(e.message)
                println(e.stackTrace)
                spaceState.copy(
                    spaceNewName = "",
                    error = true
                )
            } catch (e: HttpException) {
                println("An error occurred while creating")
                println(e.message)
                println(e.stackTrace)
                spaceState.copy(
                    spaceNewName = "",
                    error = true
                )
            }
        }
    }

    fun updateSpace(space: SpaceDTO, id: String) {
        viewModelScope.launch {
            spaceState = try {
                val result = inventoryRepository.updateSpace(space, id)

                val spacesList = spaceState.spaces.toMutableList()

                for (i in spacesList.indices) {
                    if (spacesList[i].spaceID == result.spaceID) {
                        spacesList[i] = result
                        break
                    }
                }

                spaceState.copy(
                    spaces = spacesList,
                    spaceRowClicked = result
                )

            } catch (e: IOException) {
                println("An error occurred while updating")
                spaceState.copy(
                    error = true
                )
            } catch (e: HttpException) {
                println("An error occurred while updating")
                spaceState.copy(
                    error = true
                )
            }
        }
    }

    fun deleteSpace(id: String) {
        viewModelScope.launch {
            spaceState = try {
                inventoryRepository.deleteSpace(id)

                val spacesList = spaceState.spaces.toMutableList()

                for (i in spacesList.indices) {
                    if (spacesList[i].spaceID == id.toInt()) {
                        spacesList.removeAt(i)
                        break
                    }
                }

                spaceState.copy(
                    spaces = spacesList,
                    spaceRowClicked = Space(0, "", "")
                )

            } catch (e: IOException) {
                println("An error occurred while deleting")
                println(e.message)
                spaceState.copy(
                    error = true
                )
            } catch (e: HttpException) {
                println("An error occurred while deleting")
                println(e.response())
                spaceState.copy(
                    error = true
                )
            }
        }
    }

    fun onSpaceClicked(spaceID: String) {
        viewModelScope.launch {
            spaceState = spaceState.copy(
                isLoading = true
            )

            spaceState = try {
                val result = inventoryRepository.getSpace(spaceID)

                spaceState.copy(
                    spaceRowClicked = result,
                    error = false,
                    isLoading = false
                )
            } catch (e: IOException) {
                println("An error occurred while reading the database")
                spaceState.copy(
                    isLoading = false,
                    error = true
                )
            } catch (e: HttpException) {
                println("An error occurred while reading the database")
                spaceState.copy(
                    isLoading = false,
                    error = true
                )
            }
        }
    }

    fun onEditSpace(spaceQR: Space) {
        spaceState = spaceState.copy(
            spaceEdit = spaceQR,
            spaceEditName = spaceQR.spaceName
        )
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as InventoryApplication)
                val inventoryRepository = application.container.inventoryRepository
                SpaceViewModel(inventoryRepository = inventoryRepository)
            }
        }
    }

    fun changeSpaceName(name: String) {
        spaceState = spaceState.copy(spaceEditName = name)
    }

    fun changeNewSpaceName(name: String) {
        spaceState = spaceState.copy(spaceNewName = name)
    }

    fun recordSingleCapture(id: String) {
        onSpaceClicked(id)
    }

    init {
        getSpaces()
    }
}