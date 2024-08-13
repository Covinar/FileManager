package com.example.presentation.files.components

import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.domain.models.File
import com.example.presentation.files.models.FileItem

@Composable
fun Files(
    files: List<File>,
    listState: LazyListState,
    isSearchMode: Boolean,
    isEditMode: Boolean,
    isCreateFolderMode: Boolean,
    isDialogOpened: Boolean,
    isSelected: (File) -> Boolean,
    onItemClicked: (File) -> Unit,
    getStorageIconId: (String) -> Int,
    onCreateNewFolder: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.background(Color.White),
        state = listState
    ) {
        itemsIndexed(
            items = files,
            key = { _, file ->
                file.path + "/" + file.name
            }
        ) { index, file ->
            FileItem(
                file = file,
                isLast = index == files.size - 1,
                onClicked = {
                    onItemClicked(file)
                },
                isSearchMode = isSearchMode,
                storageIconId = getStorageIconId(file.path),
                isEditMode = isEditMode,
                isSelected = isSelected(file),
                onCreateNewFolder = {
                    onCreateNewFolder(it)
                },
                isCreateFolderMode = isCreateFolderMode,
                isDialogOpened = isDialogOpened
            )
        }

    }
}