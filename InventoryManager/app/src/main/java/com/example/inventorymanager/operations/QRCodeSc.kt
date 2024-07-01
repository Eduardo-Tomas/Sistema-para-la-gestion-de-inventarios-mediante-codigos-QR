package com.example.inventorymanager.operations

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import androidx.camera.view.TransformExperimental
import androidx.compose.ui.geometry.Rect
import com.example.inventorymanager.ui.screens.ScanViewModel
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

@ExperimentalGetImage
class QRCodeSc {
    // Configure the barcode scanner
    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
        //.enableAllPotentialBarcodes()  // Optional
        .build()

    // Get an instance of BarcodeScanner
    private val scanner = BarcodeScanning.getClient(options)

    @OptIn(TransformExperimental::class)
    fun scan(
        imageProxy: ImageProxy,
        onSuccess: (Barcode?) -> Unit,
        previewViewWidth: Int,
        previewViewHeight: Int,
        scanViewModel: ScanViewModel
    ) {
        val mediaImage = imageProxy.image

        if (mediaImage != null) {
            val inputImage = InputImage.fromMediaImage(
                mediaImage,
                imageProxy.imageInfo.rotationDegrees
            )

            // Process the image
            scanner.process(inputImage)
                .addOnSuccessListener { barcodes ->
                    // barcodes KEY multiple qr codes single image

                    // Task completed successfully
                    val barcode = barcodes.getOrNull(0)

                    onSuccess(barcode)

                    // Scale used to correctly show the bounding box on the screen
                    val scaleX = previewViewWidth.toFloat() / mediaImage.height
                    val scaleY = previewViewHeight.toFloat() / mediaImage.width

                    if (barcode == null) {
                        scanViewModel.setBoundingBox(null)
                    } else {
                        barcode.boundingBox?.let {
                            val translatedRect = Rect(
                                it.left * scaleX,
                                it.top * scaleY,
                                it.right * scaleX,
                                it.bottom * scaleY
                            )

                            // Update the view
                            scanViewModel.setBoundingBox(translatedRect)
                        }
                    }
                }
                .addOnFailureListener {
                    // Task failed with an exception

                }.addOnCompleteListener {
                    imageProxy.image?.close()
                    imageProxy.close()
                }
        }
    }

    fun scanObjects(
        imageProxy: ImageProxy,
        onSuccess: (Barcode?) -> Unit
    ) {
        val mediaImage = imageProxy.image

        if (mediaImage != null) {
            val inputImage = InputImage.fromMediaImage(
                mediaImage,
                imageProxy.imageInfo.rotationDegrees
            )

            // Process the image
            scanner.process(inputImage)
                .addOnSuccessListener { barcodes ->
                    // Task completed successfully
                    val barcode = barcodes.getOrNull(0)

                    onSuccess(barcode)
                }
                .addOnFailureListener {
                    // Task failed with an exception

                }.addOnCompleteListener {
                    imageProxy.image?.close()
                    imageProxy.close()
                }
        }
    }

    fun scanSpaces(
        imageProxy: ImageProxy,
        onSuccess: (Barcode?) -> Unit
    ) {
        val mediaImage = imageProxy.image

        if (mediaImage != null) {
            val inputImage = InputImage.fromMediaImage(
                mediaImage,
                imageProxy.imageInfo.rotationDegrees
            )

            // Process the image
            scanner.process(inputImage)
                .addOnSuccessListener { barcodes ->
                    // Task completed successfully
                    val barcode = barcodes.getOrNull(0)

                    onSuccess(barcode)
                }
                .addOnFailureListener {
                    // Task failed with an exception

                }.addOnCompleteListener {
                    imageProxy.image?.close()
                    imageProxy.close()
                }
        }
    }
}