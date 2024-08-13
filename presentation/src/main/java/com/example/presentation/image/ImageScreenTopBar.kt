package com.example.presentation.image

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.domain.models.File
import com.example.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageScreenTopBar(
    title: String,
    onCloseClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onInfoClicked: () -> Unit,
    onZoomClicked: (Boolean) -> Unit,
    onCopyClicked: () -> Unit,
    onMoveClicked: () -> Unit
) {

    Column(
        modifier = Modifier.background(Color.White)
    ) {
        TopAppBar(
            title = {
                    Text(
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
            },
            navigationIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .clickable {
                            onCloseClicked()
                        }
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.White
            )
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_zoom_in),
                contentDescription = null,
                modifier = Modifier.clickable {
                    onZoomClicked(true)
                }
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_zoom_out),
                contentDescription = null,
                modifier = Modifier.clickable {
                    onZoomClicked(false)
                }
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_information),
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        onInfoClicked()
                    }
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_copy),
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        onCopyClicked()
                    }
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_move),
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        onMoveClicked()
                    }
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_delete),
                contentDescription = null,
                modifier = Modifier.clickable {
                    onDeleteClicked()
                }
            )
        }
        Divider(
            thickness = 2.dp,
            color = Color.Black
        )
    }

}