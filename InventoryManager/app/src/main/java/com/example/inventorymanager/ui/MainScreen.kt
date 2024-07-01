package com.example.inventorymanager.ui

import android.os.Build
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inventorymanager.ui.screens.AddObjectCard
import com.example.inventorymanager.ui.screens.AddSpaceCard
import com.example.inventorymanager.ui.screens.BeginningCard
import com.example.inventorymanager.ui.screens.CameraCard
import com.example.inventorymanager.ui.screens.DetectionViewModel
import com.example.inventorymanager.ui.screens.DetectionsBySpaceCard
import com.example.inventorymanager.ui.screens.DetectionsInSpaceCard
import com.example.inventorymanager.ui.screens.DetectionsObjectCard
import com.example.inventorymanager.ui.screens.EditDeleteObjectCard
import com.example.inventorymanager.ui.screens.EditDeleteSpaceCard
import com.example.inventorymanager.ui.screens.EditObjectCard
import com.example.inventorymanager.ui.screens.EditSpaceCard
import com.example.inventorymanager.ui.screens.InputStockLimit
import com.example.inventorymanager.ui.screens.NotificationObjectCard
import com.example.inventorymanager.ui.screens.NotificationPeriod
import com.example.inventorymanager.ui.screens.NotificationPeriodOption
import com.example.inventorymanager.ui.screens.NotificationTypeCard
import com.example.inventorymanager.ui.screens.NotificationsCard
import com.example.inventorymanager.ui.screens.NotificationSpaceCard
import com.example.inventorymanager.ui.screens.NotificationViewModel
import com.example.inventorymanager.ui.screens.ObjectScreen
import com.example.inventorymanager.ui.screens.ObjectViewModel
import com.example.inventorymanager.ui.screens.OneObjectCaptureCard
import com.example.inventorymanager.ui.screens.OneSpaceCaptureCard
import com.example.inventorymanager.ui.screens.PDFViewModel
import com.example.inventorymanager.ui.screens.PDFViewerCard
import com.example.inventorymanager.ui.screens.ReportList
import com.example.inventorymanager.ui.screens.ScanOptionsCard
import com.example.inventorymanager.ui.screens.ScanViewModel
import com.example.inventorymanager.ui.screens.SpaceViewModel
import com.example.inventorymanager.ui.screens.SpacesCard
import com.example.inventorymanager.ui.screens.UpdateStockLimit

// You can define an app's routes using an enum class.
// Enum classes in Kotlin have a name property
// that returns a string with the property name.
enum class InventoryManagerScreen {
    Start,
    Sc,
    ScanOptions,
    ObjectsList,
    NewObject,
    OneObjectCapture,
    ViewObjectInfo,
    EditObject,
    SpacesList,
    NewSpace,
    OneSpaceCapture,
    ViewSpaceInfo,
    EditSpace,
    DetectionsBySpace,
    DetectionsInSpace,
    ViewDetectedMissingObject,
    ReportList,
    PDFViewer,
    Notifications,
    NotificationList,
    InputStockLimit,
    SetSpaceNotification,
    SetObjectNotification,
    UpdateStockLimit,
    NotificationPeriodOption,
    SetNotificationPeriod
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalGetImage::class)
@Composable
fun MainScreen(
    // Spread this
    navController: NavHostController = rememberNavController()
) {
    val scanViewModel: ScanViewModel = viewModel(
        factory = ScanViewModel.Factory
    )

    val objectViewModel: ObjectViewModel = viewModel(
        factory = ObjectViewModel.Factory
    )

    val spaceViewModel: SpaceViewModel = viewModel(
        factory = SpaceViewModel.Factory
    )

    val detectionViewModel: DetectionViewModel = viewModel(
        factory = DetectionViewModel.Factory
    )

    val pdfViewModel: PDFViewModel = viewModel(
        factory = PDFViewModel.Factory
    )

    val notificationViewModel: NotificationViewModel = viewModel(
        factory = NotificationViewModel.Factory
    )

    // Composable acting as a container for displaying the
    // current destination of the NavGraph.
    NavHost(
        navController = navController,
        startDestination = InventoryManagerScreen.Start.name,
        modifier = Modifier
    ) {
        composable(route = InventoryManagerScreen.Start.name) {
            BeginningCard(
                onCaptureClicked = {
                    scanViewModel.getObjects()
                    scanViewModel.getSpaces()
                    //navController.navigate(InventoryManagerScreen.Sc.name)

                    navController.navigate((InventoryManagerScreen.ScanOptions.name))
                },
                onViewObjectsClicked = {
                    navController.navigate(InventoryManagerScreen.ObjectsList.name)
                },
                onViewSpacesClicked = {
                    navController.navigate(InventoryManagerScreen.SpacesList.name)
                },
                onViewDetectionsClicked = {
                    detectionViewModel.getSpaces()
                    detectionViewModel.getObjects()
                    navController.navigate(InventoryManagerScreen.DetectionsBySpace.name)
                },
                showReportTypes = {
                    navController.navigate(InventoryManagerScreen.ReportList.name)
                },
                onViewNotificationsClicked = {
                    navController.navigate(InventoryManagerScreen.Notifications.name)
                }
            )
        }

        composable(route = InventoryManagerScreen.Sc.name) {
            CameraCard(scanViewModel = scanViewModel)
        }

        composable(route = InventoryManagerScreen.ScanOptions.name) {
            ScanOptionsCard(
                scanViewModel = scanViewModel,
                navController = navController
            )
        }

        composable(route = InventoryManagerScreen.ObjectsList.name) {
            ObjectScreen(
                navController = navController,
                objectViewModel = objectViewModel
            )
        }

        composable(route = InventoryManagerScreen.NewObject.name) {
            AddObjectCard(
                navController = navController,
                objectViewModel = objectViewModel
            )
        }

        composable(route = InventoryManagerScreen.OneObjectCapture.name) {
            OneObjectCaptureCard(
                navController = navController,
                objectViewModel = objectViewModel
            )
        }

        composable(route = InventoryManagerScreen.ViewObjectInfo.name) {
            EditDeleteObjectCard(
                navController = navController,
                objectViewModel = objectViewModel
            )
        }

        composable(route = InventoryManagerScreen.EditObject.name) {
            EditObjectCard(
                navController = navController,
                objectViewModel = objectViewModel
            )
        }

        composable(route = InventoryManagerScreen.SpacesList.name) {
            SpacesCard(
                navController = navController,
                spaceViewModel = spaceViewModel
            )
        }

        composable(route = InventoryManagerScreen.NewSpace.name) {
            AddSpaceCard(
                navController = navController,
                spaceViewModel = spaceViewModel
            )
        }
        
        composable(route = InventoryManagerScreen.OneSpaceCapture.name) {
            OneSpaceCaptureCard(
                navController = navController,
                spaceViewModel = spaceViewModel
            )
        }

        composable(route = InventoryManagerScreen.ViewSpaceInfo.name) {
            EditDeleteSpaceCard(
                navController = navController,
                spaceViewModel = spaceViewModel
            )
        }

        composable(route = InventoryManagerScreen.EditSpace.name) {
            EditSpaceCard(
                navController = navController,
                spaceViewModel = spaceViewModel
            )
        }

        composable(route = InventoryManagerScreen.DetectionsBySpace.name) {
            DetectionsBySpaceCard(
                navController = navController,
                detectionViewModel = detectionViewModel
            )
        }

        composable(route = InventoryManagerScreen.DetectionsInSpace.name) {
            DetectionsInSpaceCard(
                navController = navController,
                detectionViewModel = detectionViewModel
            )
        }

        composable(route = InventoryManagerScreen.ViewDetectedMissingObject.name) {
            DetectionsObjectCard(detectionViewModel = detectionViewModel)
        }

        composable(route = InventoryManagerScreen.ReportList.name) {
            ReportList(
                navController = navController,
                pdfViewModel = pdfViewModel
            )
        }
        
        composable(route = InventoryManagerScreen.PDFViewer.name) {
            PDFViewerCard(pdfViewModel = pdfViewModel)
        }

        composable(route = InventoryManagerScreen.Notifications.name) {
            NotificationsCard(
                navController = navController,
                notificationViewModel = notificationViewModel
            )
        }

        composable(route = InventoryManagerScreen.NotificationList.name) {
            NotificationTypeCard(
                navController = navController,
                notificationViewModel = notificationViewModel
            )
        }

        composable(route = InventoryManagerScreen.InputStockLimit.name) {
            InputStockLimit(
                navController = navController,
                notificationViewModel = notificationViewModel
            )
        }

        composable(route = InventoryManagerScreen.SetSpaceNotification.name) {
            NotificationSpaceCard(
                navController = navController,
                notificationViewModel = notificationViewModel
            )
        }

        composable(route = InventoryManagerScreen.SetObjectNotification.name) {
            NotificationObjectCard(
                navController = navController,
                notificationViewModel = notificationViewModel
            )
        }

        composable(route = InventoryManagerScreen.UpdateStockLimit.name) {
            UpdateStockLimit(
                navController = navController,
                notificationViewModel = notificationViewModel
            )
        }
        
        composable(route = InventoryManagerScreen.NotificationPeriodOption.name) {
            NotificationPeriodOption(
                notificationViewModel = notificationViewModel,
                navController = navController
            )
        }

        composable(route = InventoryManagerScreen.SetNotificationPeriod.name) {
            NotificationPeriod(
                notificationViewModel = notificationViewModel,
                navController = navController
            )
        }
    }
}