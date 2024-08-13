package com.example.presentation.image

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.domain.models.File
import com.example.presentation.R
import com.example.presentation.files.utils.DashedHorizontalDivider
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun FileDetails(
    file: File,
    onCloseClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    ){
        Column {
            Divider(
                thickness = 2.dp,
                color = Color.Black
            )
            Row(
                modifier = Modifier.padding(start = 8.dp, top = 8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.file_details),
                    modifier = Modifier.weight(1F),
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 8.dp, top = 4.dp)
                        .clickable {
                            onCloseClicked()
                        }
                        .width(16.dp)
                        .height(16.dp)

                )
            }
            Column(
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.file_details_name),
                    fontWeight = FontWeight.Bold
                )
                Text(text = file.name)
                DashedHorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
                Text(
                    text = stringResource(id = R.string.file_details_size),
                    fontWeight = FontWeight.Bold
                )
                Text(text = file.formattedSize)
                DashedHorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
                Text(
                    text = stringResource(id = R.string.file_details_date_created),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text =  SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(file.date)),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }

}