package com.example.presentation.files.models

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.presentation.R

@Composable
fun MoreOptionsDialog(
    modifier: Modifier = Modifier,
    onCreateClicked: () -> Unit,
    onSortClicked: () -> Unit,
    onEditClicked: () -> Unit,
    onCancelClicked: () -> Unit
) {

    Box(
        modifier = modifier
            .background(Color.White)
            .clickable(enabled = false) {  }
    ) {
        Column {
            Divider(
                thickness = 2.dp,
                color = Color.Black
            )
            Text(
                text = stringResource(id = R.string.more_options),
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .align(Alignment.CenterHorizontally),
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                shape = AbsoluteRoundedCornerShape(8.dp),
                border = BorderStroke(2.dp, Color.Black),
                onClick = {
                    onCreateClicked()
                }
            ) {
                Text(
                    text = stringResource(id = R.string.create_new_folder)
                )
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                shape = AbsoluteRoundedCornerShape(8.dp),
                border = BorderStroke(2.dp, Color.Black),
                onClick = {
                    onSortClicked()
                }
            ) {
                Text(
                    text = stringResource(id = R.string.sort_content)
                )
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                shape = AbsoluteRoundedCornerShape(8.dp),
                border = BorderStroke(2.dp, Color.Black),
                onClick = {
                    onEditClicked()
                }
            ) {
                Text(
                    text = stringResource(id = R.string.edit)
                )
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                shape = AbsoluteRoundedCornerShape(8.dp),
                onClick = {
                    onCancelClicked()
                }
            ) {
                Text(
                    text = stringResource(id = R.string.cancel)
                )
            }
        }
    }

}