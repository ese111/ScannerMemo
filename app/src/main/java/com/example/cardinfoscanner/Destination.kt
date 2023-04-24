package com.example.cardinfoscanner

import android.os.Bundle
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

interface Destination {
    val route: String
    val screen: @Composable (navController: NavHostController, arguments: Bundle) -> Unit

    companion object {
        internal const val cameraHomeRoute = "home/cam"
        internal const val permissionRoute = "home/cam/permission"
        internal const val cameraRoute = "home/cam/scan"
        internal const val noteHomeRout = "home/note"
        internal const val noteListRout = "home/note/list"
        internal const val settingHomeRout = "home/setting"
        internal const val settingRout = "home/setting/main"
    }
}

