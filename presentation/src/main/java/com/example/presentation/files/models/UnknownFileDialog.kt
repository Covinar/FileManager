package com.example.presentation.files.models

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.presentation.R
import com.example.presentation.files.utils.DashedHorizontalDivider

@Composable
fun UnknownFileDialog(
    modifier: Modifier = Modifier,
    title: String,
    onCloseClicked: () -> Unit,
    onInfoClicked: () -> Unit
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
            Row(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = null,
                    modifier = Modifier
                        .width(32.dp)
                        .height(32.dp)
                        .padding(start = 8.dp, end = 8.dp)
                        .clickable {
                        onCloseClicked()
                    }
                )
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(1F)
                        .padding(end = 8.dp)
                        .align(Alignment.CenterVertically),
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_information),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .clickable {
                            onInfoClicked()
                        }
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_copy),
                    contentDescription = null,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_move),
                    contentDescription = null,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = null,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
            DashedHorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.unknown_file_dialog_title),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center

                )
                Text(
                    text = stringResource(id = R.string.unknown_file_dialog_description),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

}