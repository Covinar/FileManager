package com.example.presentation.image

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun ImagePreviewScreen(
    path: String,
    viewModel: ImagePreviewViewModel = hiltViewModel(),
    onCloseClicked: () -> Unit,
) {

    LaunchedEffect(key1 = "Image_preview_screen") {
        viewModel.getFile(path)
    }

    val state = viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            ImageScreenTopBar(
                title = state.value.file?.name.orEmpty(),
                onCloseClicked = {
                    onCloseClicked()
                },
                onDeleteClicked = {
                    viewModel.changeDeleteMode()
                },
                onInfoClicked = {
                    viewModel.infoClicked()
                },
                onZoomClicked = { increase ->
                    viewModel.zoom(increase)
                },
                onCopyClicked = {

                },
                onMoveClicked = {

                }
            )
        }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(it)
        ) {
            GlideImage(
                model = path,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleY = state.value.zoom
                        scaleX = state.value.zoom
                    },

            )
            if (state.value.isInfoClicked) {
                state.value.file?.let { file ->
                    FileDetails(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        onCloseClicked = {
                            viewModel.infoClicked()
                        },
                        file = file
                    )
                }
            }
            if (state.value.isDeleteMode) {
                state.value.file?.name?.let { name ->
                    DialogDelete(
                        modifier = Modifier
                            .padding(it)
                            .align(Alignment.BottomCenter),
                        fileName = name,
                        onBackButtonClicked = {
                            viewModel.changeDeleteMode()
                        },
                        onDeleteClicked = {
                            viewModel.deleteFile()
                            onCloseClicked()
                        }
                    )
                }
            }
        }

    }

}