package com.example.presentation.image

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.presentation.R

@Composable
fun DialogDelete(
    fileName: String,
    modifier: Modifier = Modifier,
    onBackButtonClicked: () -> Unit,
    onDeleteClicked: () -> Unit
) {
    
    Box(
        modifier = modifier
            .background(Color.White)
            .clickable(enabled = false) {  }
    ) {
        Column {
            Divider(
                thickness = 4.dp,
                color = Color.Black
            )
            Text(
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp),
                text = stringResource(id = R.string.delete_dialog_title, fileName),
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                text = stringResource(id = R.string.delete_dialog_description)
            )
            Row(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Button(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .weight(1F),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = Color.Black
                    ),
                    shape = AbsoluteRoundedCornerShape(8.dp),
                    onClick = {
                        onBackButtonClicked()
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.back),
                        fontWeight = FontWeight.Bold
                    )
                }
                Button(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .weight(1F),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        containerColor = Color.White
                    ),
                    shape = AbsoluteRoundedCornerShape(8.dp),
                    border = BorderStroke(2.dp, Color.Black),
                    onClick = {
                        onDeleteClicked()
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.delete),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
    
}