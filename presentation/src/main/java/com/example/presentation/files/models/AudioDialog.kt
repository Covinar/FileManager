package com.example.presentation.files.models

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.models.File
import com.example.presentation.R
import com.example.presentation.files.utils.SpeedLevel
import com.example.presentation.files.utils.toStringDuration

@SuppressLint("ResourceType")
@Composable
fun AudioDialog(
    file: File,
    speedLevel: SpeedLevel,
    isAudioPaused: Boolean,
    duration: Int,
    currentPosition: State<Double>,
    onSliderPositionChanged: (Float) -> Unit,
    onSpeedClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    onInfoClicked: () -> Unit,
    onPauseClicked: () -> Unit,
    onForwardClicked: () -> Unit,
    onBackwardClicked: () -> Unit,
    onBackToStartClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    val position = remember {
        mutableFloatStateOf(0.0F)
    }

    LaunchedEffect(key1 = currentPosition.value) {
        position.floatValue = currentPosition.value.toFloat()
    }

    Box(
        modifier = modifier.background(Color.White)
    ) {
        Column {
            Divider(
                thickness = 2.dp,
                color = Color.Black
            )
            Row(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close), 
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                        .clickable {
                            onCloseClicked()
                        }
                )
                Text(
                    text = file.name,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.SpaceAround

            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_information),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            onInfoClicked()
                        }
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_copy),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_move),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            onDeleteClicked()
                        }
                )
            }
            Column(
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Slider(
                    value = position.floatValue,
                    onValueChange = {
                        position.floatValue = it
                        onSliderPositionChanged(it)
                    }
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = toStringDuration((duration * currentPosition.value).toInt()),
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                    Text(
                        text = toStringDuration(duration),
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = speedLevel.levelId),
                    modifier = Modifier
                        .padding(bottom = 25.dp, end = 8.dp)
                        .align(Alignment.BottomEnd)
                        .clickable {
                            onSpeedClicked()
                        },
                    fontSize = 24.sp
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_backward_to_start),
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            onBackToStartClicked()
                        }
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_backward),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .clickable {
                                onBackwardClicked()
                            }
                    )
                    val painter = if (isAudioPaused) {
                        painterResource(id = R.drawable.ic_play)
                    } else {
                        painterResource(id = R.drawable.ic_stop)
                    }
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            onPauseClicked()
                        }
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_forward),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .clickable {
                                onForwardClicked()
                            }
                    )
                    Box(modifier = Modifier.size(25.dp, 23.dp))
                }
            }
        }
    }
    
}