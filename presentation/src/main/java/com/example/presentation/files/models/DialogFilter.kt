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
import com.example.domain.models.FilterType
import com.example.presentation.R

@Composable
fun DialogFilter(
    modifier: Modifier = Modifier,
    onCancelClicked: () -> Unit,
    onButtonClicked: (filterType: FilterType) -> Unit
) {

    Box(
        modifier = modifier
            .background(Color.White)
            .clickable(enabled = false) {}
        ) {
        Column {
            Divider(
                thickness = 2.dp,
                color = Color.Black
            )
            Text(
                text = stringResource(id = R.string.filter_dialog_title),
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
                    onButtonClicked(FilterType.IMAGES)
                }
            ) {
                Text(
                    text = stringResource(id = R.string.images)
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
                    onButtonClicked(FilterType.AUDIO)
                }
            ) {
                Text(
                    text = stringResource(id = R.string.audio)
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
                    onButtonClicked(FilterType.EBOOKS)
                }
            ) {
                Text(
                    text = stringResource(id = R.string.ebooks)
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