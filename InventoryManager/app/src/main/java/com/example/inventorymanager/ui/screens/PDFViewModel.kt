package com.example.inventorymanager.ui.screens

import android.content.Context
import android.net.Uri
import android.os.Environment
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
import com.example.inventorymanager.model.PDF
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.VerticalPdfReaderState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException

class PDFViewModel(
    private val inventoryRepository: InventoryRepository
) : ViewModel() {
    var pdfState: PDFState by mutableStateOf(PDFState())
        private  set

    fun getSpaces() {
        viewModelScope.launch {
            pdfState = pdfState.copy(
                isLoading = true
            )

            pdfState = try {
                val result = inventoryRepository.getSpaces()

                pdfState.copy(
                    spaces = result,
                    error = false,
                    isLoading = false
                )
            } catch (e: IOException) {
                println("An error occurred while reading the database")
                pdfState.copy(
                    isLoading = false,
                    error = true
                )
            } catch (e: HttpException) {
                println("An error occurred while reading the database")
                pdfState.copy(
                    isLoading = false,
                    error = true
                )
            }
        }
    }

    fun changeStartDate(value: String) {
        pdfState = pdfState.copy(startDate = value)
    }

    fun changeEndDate(value: String) {
        pdfState = pdfState.copy(endDate = value)
    }

    fun setSelectedSpaces(selectedItems: MutableList<Int>) {
        pdfState = pdfState.copy(selectedSpaces = selectedItems)
    }

    fun onReportSelected(reportType: Int) {
        pdfState = pdfState.copy(reportType = reportType)
    }

    fun setReportName(name: String) {
        pdfState = pdfState.copy(reportName = name)
    }

    fun setLocation(value: String) {
        pdfState = pdfState.copy(pdfLocation = value)
    }

    fun setExistAndNoDir(value: Boolean) {
        pdfState = pdfState.copy(existAndNoDir = value)
    }

    fun getReport(
        pdf: PDF,
        name: String,
        type: Int,
        context: Context
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            pdfState = pdfState.copy(isLoadingToShow = true)

            try {
                var response: ResponseBody? = null

                if (type == 1) {
                    response = inventoryRepository.getSpacesReport(pdf)
                } else if (type == 2) {
                    response = inventoryRepository.getDetailedReport(pdf)
                }

                if (response != null) {
                    val responseBytes = response.bytes()

                    // Save the file in cache
                    val pdfFile = File(context.cacheDir, name)
                    pdfFile.writeBytes(responseBytes)

                    pdfState = pdfState.copy(
                        pdfLocation = pdfFile.toURI().toString(),
                        error = false
                    )

                    if (Uri.parse(pdfState.pdfLocation) != null) {
                        //val file = File(Uri.parse(pdfState.pdfLocation).path)

                        Uri.parse(pdfState.pdfLocation).path?.let {
                            val file = File(it)

                            if (file.exists() && !file.isDirectory) {
                                setPdfVerticalReaderState(Uri.parse(pdfState.pdfLocation))

                                println(file.exists() && !file.isDirectory)
                                println("Download Finished")
                                pdfState = pdfState.copy(existAndNoDir = true)
                            } else {
                                pdfState = pdfState.copy(existAndNoDir = false)
                                println(file.exists() && !file.isDirectory)
                            }
                        }
                    }
                }
            } catch (e: IOException) {
                println("An error occurred while reading or saving the pdf")
                e.printStackTrace()
                pdfState = pdfState.copy(
                    error = true
                )

            } catch (e: HttpException) {
                e.printStackTrace()
                println("An error occurred while reading or saving the pdf")
                pdfState = pdfState.copy(
                    error = true
                )
            }

            pdfState = pdfState.copy(isLoadingToShow = false)
        }
    }

    fun downloadReport(
        pdf: PDF,
        name: String,
        type: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            pdfState = pdfState.copy(toastMessage = "Iniciando descarga ...")

            try {
                var response: ResponseBody? = null

                if (type == 1) {
                    response = inventoryRepository.getSpacesReport(pdf)
                } else if (type == 2) {
                    response = inventoryRepository.getDetailedReport(pdf)
                }

                if (response != null) {
                    val timestamp = System.currentTimeMillis().toString()

                    val responseBytes = response.bytes()

                    val downloadFolder = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS
                    )

                    // Save the file
                    val destinationFile = File(
                        downloadFolder.absolutePath,
                        "/inventory/${name}${timestamp}.pdf"
                    )
                    destinationFile.writeBytes(responseBytes)

                    println("Saved")
                    pdfState = pdfState.copy(toastMessage = "Descarga finalizada")
                }
            } catch (e: IOException) {
                println("An error occurred while downloading the pdf")
                pdfState = pdfState.copy(
                    error = true
                )

                pdfState = pdfState.copy(toastMessage = "Descarga fallida")

            } catch (e: HttpException) {
                println("An error occurred while downloading the pdf")
                pdfState = pdfState.copy(
                    error = true
                )

                pdfState = pdfState.copy(toastMessage = "Descarga fallida")
            }
        }
    }

    fun setPdfVerticalReaderState(uri: Uri) {
        val pdfVerticalReaderState = VerticalPdfReaderState(
            resource = ResourceType.Local(uri),
            isZoomEnable = true
        )

        pdfState = pdfState.copy(pdfVerticalReaderState = pdfVerticalReaderState)
    }

    fun setToastMessage(value: String) {
        viewModelScope.launch(Dispatchers.IO) {
            pdfState = pdfState.copy(toastMessage = value)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as InventoryApplication)
                val inventoryRepository = application.container.inventoryRepository
                PDFViewModel(inventoryRepository = inventoryRepository)
            }
        }
    }

    init {
        getSpaces()
    }
}