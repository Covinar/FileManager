package com.example.presentation.main

sealed class Screen(
    val route: String
) {

    data object Files: Screen(FILES_ROUTE)

    data object ImagePreview: Screen(IMAGE_PREVIEW_ROUTE) {
        const val ARGS_IMAGE = "/{image}"
        const val IMAGE = "image"
    }

    companion object {

        const val IMAGE_PREVIEW_ROUTE = "image_preview"
        const val FILES_ROUTE = "files"

    }

}