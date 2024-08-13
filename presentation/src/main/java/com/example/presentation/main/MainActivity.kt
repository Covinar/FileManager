package com.example.presentation.main

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.presentation.files.FilesScreen
import com.example.presentation.image.ImagePreviewScreen
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val postNotificationPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        isPostNotificationGranted = it
    }

    private var isPostNotificationGranted by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = Screen.Files.route
            ) {
                composable(
                    route = Screen.Files.route
                ) {
                    if (isPostNotificationGranted) {
                        FilesScreen(
                            onImageClicked = {
                                val string = it.replace('/', ']')
                                navController.navigate("${Screen.ImagePreview.route}/${string}")
                            }
                        )
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            postNotificationPermissionRequest.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                }
                composable(
                    route = Screen.ImagePreview.route + Screen.ImagePreview.ARGS_IMAGE,
                    arguments = listOf(
                        navArgument(
                            name = Screen.ImagePreview.IMAGE,
                        ) {
                            type = NavType.StringType
                        }
                    )
                ) { backStackEntry ->
                    val path = backStackEntry.arguments?.getString(Screen.ImagePreview.IMAGE)
                    path?.let {
                        val string = path.replace(']', '/')
                        ImagePreviewScreen(
                            path = string,
                            onCloseClicked = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }

}
