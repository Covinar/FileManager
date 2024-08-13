package com.example.presentation.files.models

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.presentation.R

@Composable
fun DeleteDialog(
    modifier: Modifier = Modifier,
    selectedFilesCount: Int,
    onBackClicked: () -> Unit,
    onDeleteClicked: () -> Unit
) {

    Box(
        modifier = modifier
            .background(Color.White)
            .fillMaxWidth()
            .clickable(enabled = false) {  }
    ) {
        Column() {
            Divider(
                thickness = 2.dp,
                color = Color.Black
            )
            Text(
                text = pluralStringResource(
                    id = R.plurals.delete_dialog_title,
                    count = selectedFilesCount,
                    selectedFilesCount
                ),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp, top = 8.dp)
            )
            Text(
                text = stringResource(id = R.string.delete_dialog_description),
                modifier = Modifier.padding(start = 8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Button(
                    onClick = {
                        onBackClicked()
                    },
                    modifier = Modifier
                        .weight(1F)
                        .padding(end = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.Black
                    ),
                    shape = AbsoluteRoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.delete_dialog_back_button),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                Button(
                    onClick = {
                        onDeleteClicked()
                    },
                    modifier = Modifier
                        .weight(1F)
                        .padding(start = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    ),
                    shape = AbsoluteRoundedCornerShape(8.dp),
                    border = BorderStroke(2.dp, Color.Black)
                ) {
                    Text(
                        text = stringResource(id = R.string.delete_dialog_delete_button),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}