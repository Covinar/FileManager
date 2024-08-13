package com.example.presentation.files.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.presentation.R

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    isSearchMode: Boolean,
    isFilterMode: Boolean
) {

    val painter = when {
        isSearchMode -> R.string.searching
        isFilterMode -> R.string.filtering
        else -> R.string.loading
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = painter)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_loading),
            contentDescription = null
        )
    }


}