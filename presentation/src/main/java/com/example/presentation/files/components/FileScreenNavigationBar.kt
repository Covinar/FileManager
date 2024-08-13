package com.example.presentation.files.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.presentation.R

@Composable
fun FileScreenNavigationBar(
    titles: List<String>,
    overflowCounter: Int,
    onBackToDirectory: (count: Int) -> Unit,
    onOverflow: () -> Unit,
    onHomeClicked: () -> Unit
) {

    var state by remember {
        mutableStateOf(titles)
    }

    val new = titles.toMutableList()
    repeat(overflowCounter) {
        new.removeFirstOrNull()
    }
    state = new

    Row(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_home),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .clickable {
                    onHomeClicked()
                }
        )
        if (overflowCounter > 0) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow),
                contentDescription = null
            )
            Text(
                text = stringResource(id = R.string.overflow_text)
            )
        }
        state.forEachIndexed { index, title ->
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow),
                contentDescription = null
            )
            Text(
                text = if (title.length < 25) title else title.take(24) + stringResource(id = R.string.overflow_text),
                maxLines = 1,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .align(Alignment.CenterVertically)
                    .clickable {
                        onBackToDirectory(index + overflowCounter)
                    },
                fontWeight = if (state.lastIndex == index) FontWeight.Bold else FontWeight.Normal,
                onTextLayout = {
                    if (it.hasVisualOverflow) {
                        onOverflow()
                    }
                },
            )
        }

    }
}