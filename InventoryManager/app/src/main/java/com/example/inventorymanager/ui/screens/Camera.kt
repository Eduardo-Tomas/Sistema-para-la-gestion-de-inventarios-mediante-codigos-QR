package com.example.inventorymanager.ui.screens

import android.os.Build
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.inventorymanager.R
import com.example.inventorymanager.model.DetectionDTO
import com.example.inventorymanager.operations.DateTime
import com.example.inventorymanager.operations.QRCodeSc
import com.google.mlkit.vision.barcode.common.Barcode
import java.util.concurrent.Executors

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@ExperimentalGetImage
fun CameraCard(
    scanViewModel: ScanViewModel,
    modifier: Modifier = Modifier
) {
    // To show the content of the QR code when an object is detected
    var scannedObjectQRCode by remember {
        mutableStateOf<String?>(null)
    }

    Box(modifier = modifier) {
        Preview(
            onQRScanned = { qrCode ->
                /*
                    displayValue
                    Returns barcode value in a user-friendly format.
                    This method may omit some of the information encoded
                    in the barcode.
                    For example, if getRawValue() returns
                    'MEBKM:TITLE:Google;URL://www.google.com;;',
                    the display value might be '//www.google.com'.

                    This value may be multiline, for example, when line
                    breaks are encoded into the original TEXT barcode
                    value. May include the supplement value.

                    Returns null if nothing found.
                 */
                val qrValue = qrCode?.displayValue

                qrValue?.let {
                    val idStr = it.substring(1)

                    if (isNumber(idStr)) {
                        val id = idStr.toInt()

                        val objectList = scanViewModel.scanState.objects
                        val spaceList = scanViewModel.scanState.spaces

                        // Detect an object
                        if (it.startsWith("0") && objectList.contains(id)) {
                            // Update view
                            scannedObjectQRCode = id.toString()

                            val d = DateTime()
                            val currentDate = d.getCurrentDate()
                            val currentTime = d.getCurrentTime()

                            if (currentDate != null && currentTime != null) {
                                val detected = DetectionDTO(
                                    id,
                                    -1,
                                    currentDate.toString(),
                                    currentTime.toString()
                                )

                                scanViewModel.onObjectDetected(detected)
                            }

                        } else if (it.startsWith("1") && spaceList.contains(id)) {
                            // Update view
                            scanViewModel.setScannedSpaceQRCode(id.toString())

                            scanViewModel.onSpaceDetected(id)
                        }
                    }
                }
            },
            scanViewModel = scanViewModel
        )
    }

    if (scannedObjectQRCode != null) {
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(8.dp))
                    .background(color = colorResource(id = R.color.secondary))
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.object_detected),
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.accent),
                    modifier = Modifier.padding(
                        start = 8.dp,
                        top = 8.dp,
                        end = 8.dp
                    )
                )

                Text(
                    text = scannedObjectQRCode.toString(),
                    color = colorResource(id = R.color.accent),
                    modifier = Modifier.padding(
                        start = 8.dp,
                        bottom = 8.dp,
                        end = 8.dp
                    )
                )
            }
        }
    }

    if(scanViewModel.scanState.scannedSpaceQRCode != null) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(8.dp))
                    .background(color = colorResource(id = R.color.secondary))
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.space_detected),
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.accent),
                    modifier = Modifier.padding(
                        start = 8.dp,
                        top = 8.dp,
                        end = 8.dp
                    )
                )

                Text(
                    text = scanViewModel.scanState.scannedSpaceQRCode.toString(),
                    color = colorResource(id = R.color.accent),
                    modifier = Modifier.padding(
                        start = 8.dp,
                        bottom = 8.dp,
                        end = 8.dp
                    )
                )
            }
        }
    }

    val rect = scanViewModel.scanState.boundingBox
    if (rect != null) {
        val rSize = rect.size
        val rTopLeft = rect.topLeft

        Canvas(modifier = Modifier.fillMaxSize()) {
            rotate(degrees = 0f) {
                drawRect(
                    color = Color.Cyan,
                    topLeft = rTopLeft,
                    size = rSize,
                    style = Stroke(
                        width = 8f
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalGetImage::class)
@Composable
fun Preview(
    onQRScanned: (Barcode?) -> Unit,
    scanViewModel: ScanViewModel
) {
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
                scaleType = /*PreviewView.ScaleType.FILL_START*/PreviewView.ScaleType.FILL_CENTER

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
                        camera.scan(
                            imageProxy = imageProxy,
                            onSuccess = onQRScanned,

                            previewViewWidth = this.width,
                            previewViewHeight = this.height,
                            scanViewModel = scanViewModel
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