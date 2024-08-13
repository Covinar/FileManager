package com.example.presentation.files.components

import androidx.compose.foundation.background
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.presentation.files.utils.StorageType

@Composable
fun FileScreenTab(
    selectedStorageType: StorageType,
    onTabClicked: (index: Int) -> Unit,
    isCreateFolderMode: Boolean,
    isDialogOpened: Boolean
    ) {

    val titles = listOf("Phone", "SD Card")

    TabRow(
        selectedTabIndex = selectedStorageType.ordinal,
    ) {
        titles.forEachIndexed { index, title ->
            Tab(
                selected = selectedStorageType.ordinal == index,
                modifier = Modifier.background(Color.White),
                text = {
                    Text(
                        text = title,
                        color = Color.Black
                    )
                },
                onClick = {
                    if (!isCreateFolderMode && !isDialogOpened) {
                        onTabClicked(index)
                    }
                }
            )
        }
    }
}