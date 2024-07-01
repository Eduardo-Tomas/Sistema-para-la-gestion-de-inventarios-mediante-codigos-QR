package com.example.inventorymanager.ui.screens

import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.inventorymanager.operations.QRCodeSc
import com.example.inventorymanager.ui.InventoryManagerScreen
import com.google.mlkit.vision.barcode.common.Barcode
import java.util.concurrent.Executors

@Composable
fun OneObjectCaptureCard(
    navController: NavHostController,
    objectViewModel: ObjectViewModel
) {
    var isScannedQRCode by remember { mutableStateOf(false) }

    val objectList = objectViewModel.objectState.objects.map { it.objectID }

    PreviewObject(
        onQRScanned = { qrCode ->
            if (!isScannedQRCode) {
                val qrValue = qrCode?.displayValue

                qrValue?.let {
                    val idStr = it.substring(1)

                    if (isNumber(idStr)) {
                        val id = idStr.toInt()

                        if (it.startsWith("0") && objectList.contains(id)) {
                            isScannedQRCode = true
                            navController.navigate(InventoryManagerScreen.ViewObjectInfo.name)

                            objectViewModel.recordSingleCapture(id.toString())
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalGetImage::class)
@Composable
fun PreviewObject(onQRScanned: (Barcode?) -> Unit) {
    val camera = remember { QRCodeSc() }

    var imageCapture: ImageCapture

    val lifecycleOwner = LocalLifecycleOwner.current

    // CameraX does not support Jetpack Compose so an AndroidView is used
    AndroidView(
        factory = { context ->
            // PreviewView is defined in CameraX
            PreviewView(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    // Equivalent to Modifier.fillMaxWidth().fillMaxHeight()
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                scaleType = PreviewView.ScaleType.FILL_START

                // Create an instance of the ProcessCameraProvider. This is used to bind the
                // lifecycle of cameras to the lifecycle owner. This eliminates the task of
                // opening and closing the camera since CameraX is lifecycle-aware.
                val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

                // Add a listener to the cameraProviderFuture. Add ContextCompat.getMainExecutor()
                // as the second argument. This returns an Executor that runs on the main thread.
                cameraProviderFuture.addListener({
                    // Add a ProcessCameraProvider. This is used to bind the lifecycle of our camera to the
                    // LifecycleOwner within the application's process.
                    val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                    // Preview
                    // Initialize our Preview object, call build on it, get a surface provider from
                    // previewView, and then set it on the preview.
                    val preview = androidx.camera.core.Preview.Builder()
                        .build()
                        .also {
                            it.setSurfaceProvider(this.surfaceProvider)
                        }

                    imageCapture = ImageCapture.Builder().build()

                    val executor = Executors.newSingleThreadExecutor()

                    val imageAnalyzer = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()

                    imageAnalyzer.setAnalyzer(executor) { imageProxy ->
                        camera.scanObjects(
                            imageProxy = imageProxy,
                            onSuccess = onQRScanned
                        )
                    }

                    // Create a CameraSelector object and select DEFAULT_BACK_CAMERA.
                    // Select back camera as a default
                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    // Create a try block. Inside that block, make sure nothing is bound to the
                    // cameraProvider, and then bind our cameraSelector and preview object to the cameraProvider.
                    try {
                        // Unbind use cases before rebinding
                        cameraProvider.unbindAll()

                        // Bind use cases to camera
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner, cameraSelector, preview, imageCapture, imageAnalyzer
                        )

                    } catch (e: Exception) {

                        // There are a few ways this code could fail, like if the app is no longer in focus.
                        // Wrap this code in a catch block to log if there's a failure.

                        println("Use case binding failed")
                    }
                }, ContextCompat.getMainExecutor(context))
            }
        }
    )
}