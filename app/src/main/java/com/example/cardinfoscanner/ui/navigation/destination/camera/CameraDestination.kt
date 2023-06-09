package com.example.cardinfoscanner.ui.navigation.destination.camera

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.example.cardinfoscanner.Destination
import com.example.cardinfoscanner.Destination.Companion.cameraRoute
import com.example.cardinfoscanner.navigateSingleTopToGraph
import com.example.cardinfoscanner.stateholder.app.AppState
import com.example.cardinfoscanner.stateholder.camera.rememberCameraScreenState
import com.example.cardinfoscanner.ui.camera.CameraPreViewScreen
import com.example.cardinfoscanner.ui.navigation.destination.note.NoteEditDestination
import com.example.cardinfoscanner.util.CameraUtil
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import kotlinx.coroutines.launch

object CameraDestination : Destination {
    override val route = cameraRoute
    @OptIn(ExperimentalPermissionsApi::class)
    override val screen: @Composable (NavHostController, Bundle?, AppState?) -> Unit =
        { navController, _, _ ->
            navController.currentBackStackEntry?.let {
                val state = rememberCameraScreenState()
                val cameraUtil = remember {
                    CameraUtil(state.uiState.context)
                        .addSuccessCallBack { str ->
                            state.onSuccessScanText(str)
                        }.addErrorCallBack { str ->
                            state.onErrorScanText(str)
                        }
                }
                CameraPreViewScreen(
                    state = state,
                    cameraUtil = cameraUtil,
                    navToResult = { scanText ->
                        if (scanText.isNotEmpty()) {
                            val str = scanText.replace("/", "+")
                            navController.navigateSingleTopToGraph("${NoteEditDestination.route}/$str")
                            return@CameraPreViewScreen
                        }
                        state.uiState.scope.launch {
                            state.dialogState.value = false
                            state.snackBarHostState.showSnackbar("인식된 정보가 없습니다.")
                        }
                    },
                    onUpButtonClick = navController::navigateUp,
                    moveToCamera = {
                        navController.navigateSingleTopToGraph(cameraRoute)
                    },
                    onBottomSheetDismissRequest = {
                        if(state.cameraPermissionState.permissionState.status.isGranted) {
                            state.onDismissBottomSheet()
                        } else {
                            navController.navigateUp()
                        }
                    }
                )
            }
        }
}




