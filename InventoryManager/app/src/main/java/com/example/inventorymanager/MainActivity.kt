package com.example.inventorymanager

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.inventorymanager.ui.MainScreen
import com.example.inventorymanager.ui.theme.InventoryManagerTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InventoryManagerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val permissions = mutableListOf(Manifest.permission.CAMERA)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissions.add(
                            0,
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                    }

                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }

                    val multiplePermissionsState = rememberMultiplePermissionsState(permissions)

                    LaunchedEffect(Unit) {
                        if (!multiplePermissionsState.allPermissionsGranted) {
                            multiplePermissionsState.launchMultiplePermissionRequest()
                        }
                    }

                    MainScreen()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        val context = this.applicationContext

        // Clear cache directory
        context.cacheDir.deleteRecursively()
    }
}