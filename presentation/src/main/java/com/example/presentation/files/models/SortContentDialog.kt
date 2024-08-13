package com.example.presentation.files.models

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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.presentation.R
import com.example.presentation.files.utils.SortType

@Composable
fun SortContentDialog(
    modifier: Modifier = Modifier,
    onCloseClicked: () -> Unit,
    onSortTypeClicked: (selectedOption: SortType) -> Unit
) {
        val selectedOption = remember { mutableStateOf(SortType.NAME_AZ) }

        Box(
            modifier = modifier
                .background(Color.White)
                .fillMaxWidth()
                .clickable(enabled = false) {  }
        ) {
            Divider(
                thickness = 2.dp,
                color = Color.Black
            )
            Column(
            ) {
                Row(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.sort_content),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1F)
                    )
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
                }
                Row() {
                    RadioButton(
                        selected = selectedOption.value == SortType.NAME_AZ,
                        onClick = {
                            selectedOption.value = SortType.NAME_AZ
                            onSortTypeClicked(SortType.NAME_AZ)
                        }
                    )
                    Text(
                        text = stringResource(id = R.string.common_radio_button_az),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
                Row() {
                    RadioButton(
                        selected = selectedOption.value == SortType.NAME_ZA,
                        onClick = {
                            selectedOption.value = SortType.NAME_ZA
                            onSortTypeClicked(SortType.NAME_ZA)
                        }
                    )
                    Text(
                        text = stringResource(id = R.string.common_radio_button_za),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
                Row() {
                    RadioButton(
                        selected = selectedOption.value == SortType.FROM_LARGEST_TO_SMALLEST,
                        onClick = {
                            selectedOption.value = SortType.FROM_LARGEST_TO_SMALLEST
                            onSortTypeClicked(SortType.FROM_LARGEST_TO_SMALLEST)
                        }
                    )
                    Text(
                        text = stringResource(id = R.string.common_radio_button_largest_to_smallest),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
                Row() {
                    RadioButton(
                        selected = selectedOption.value == SortType.FROM_SMALLEST_TO_LARGEST,
                        onClick = {
                            selectedOption.value = SortType.FROM_SMALLEST_TO_LARGEST
                            onSortTypeClicked(SortType.FROM_SMALLEST_TO_LARGEST)
                        }
                    )
                    Text(
                        text = stringResource(id = R.string.common_radio_button_smallest_to_largest),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
                Row() {
                    RadioButton(
                        selected = selectedOption.value == SortType.FROM_NEWEST_TO_OLDEST,
                        onClick = {
                            selectedOption.value = SortType.FROM_NEWEST_TO_OLDEST
                            onSortTypeClicked(SortType.FROM_NEWEST_TO_OLDEST)
                        }
                    )
                    Text(
                        text = stringResource(id = R.string.common_radio_button_newest_to_oldest),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
                Row() {
                    RadioButton(
                        selected = selectedOption.value == SortType.FROM_OLDEST_TO_NEWEST,
                        onClick = {
                            selectedOption.value = SortType.FROM_OLDEST_TO_NEWEST
                            onSortTypeClicked(SortType.FROM_OLDEST_TO_NEWEST)
                        }
                    )
                    Text(
                        text = stringResource(id = R.string.common_radio_button_oldest_to_newest),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
        }

}