package com.example.presentation.files.models

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import com.example.presentation.R

@Composable
fun DeletedItemsDialog(
    modifier: Modifier = Modifier,
    selectedFilesCount: Int,
    onCloseClicked: () -> Unit
) {
    Box(
        modifier = modifier
            .background(Color.White)
            .fillMaxWidth()
            .clickable(enabled = false) { }
    ) {
        Row(
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = pluralStringResource(
                    id = R.plurals.delete_dialog_title,
                    count = selectedFilesCount,
                    selectedFilesCount
                ),
                modifier = Modifier.weight(1F)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = null,
                modifier = Modifier.clickable {
                    onCloseClicked()
                }
            )
        }
    }
}