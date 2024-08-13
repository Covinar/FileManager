package com.example.presentation.files.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.presentation.R
import com.example.presentation.files.utils.StorageType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    selectedItemCount: Int,
    titles: List<String>,
    isEditMode: Boolean,
    selectedStorageType: StorageType,
    isSdCardExist: Boolean,
    isFilterDialogMode: Boolean,
    fileName: String,
    stackSize: Int,
    isDirectory: Boolean,
    isSearchMode: Boolean,
    isFilterMode: Boolean,
    isCreateFolderMode: Boolean,
    isDialogOpened: Boolean,
    overflowCounter: Int,
    onTabClicked: (index: Int) -> Unit,
    onBackClicked: () -> Unit,
    onSearchClicked: () -> Unit,
    onSearchQuery: (String) -> Unit,
    onCancelClicked: () -> Unit,
    onFilterClicked: () -> Unit,
    onMoreClicked: () -> Unit,
    onOverflow: () -> Unit,
    onBackToDirectory: (count: Int) -> Unit,
    onHomeClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    onCopyClicked: () -> Unit,
    onMoveClicked: () -> Unit,
    onDeleteClicked: () -> Unit
) {

    var searchQuery by remember { mutableStateOf("") }

    Column {
        TopAppBar(
            title = {
                if (isSearchMode) {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = searchQuery,
                        textStyle = TextStyle(fontWeight = FontWeight.Bold),
                        onValueChange = {
                            searchQuery = it
                            onSearchQuery(it)
                        },
                        placeholder = {
                            Text(text = stringResource(id = R.string.search))
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_cancel),
                                contentDescription = null,
                                modifier = Modifier.clickable {
                                    searchQuery = ""
                                    onCancelClicked()
                                }
                            )
                        }
                    )
                } else if (isFilterDialogMode){
                    Text(
                        text = when(selectedStorageType) {
                            StorageType.PHONE -> { stringResource(id = R.string.files_screen_title_phone) }
                            StorageType.SD_CARD -> { stringResource(id = R.string.files_screen_title_sd_card) }
                            },    
                        modifier = Modifier.padding(start = 8.dp),
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                } else if (isEditMode) {
                    Text(
                        text = stringResource(id = R.string.edit_mode_title, selectedItemCount),
                        modifier = Modifier
                            .padding(start = 8.dp),
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                } else {
                    Text(
                        text = fileName,
                        modifier = Modifier.padding(start = 8.dp),
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            },
            actions = {
                if (!isSearchMode && !isEditMode) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable {
                                if (!isCreateFolderMode && !isDialogOpened) {
                                    onSearchClicked()
                                }
                            }
                    )
                    if (!isFilterMode) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_filter),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .clickable {
                                    if (!isCreateFolderMode && !isDialogOpened) {
                                        onFilterClicked()
                                    }
                                }
                        )
                    }
                    Icon(
                        painter = painterResource(id = R.drawable.ic_more),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable {
                                if (!isCreateFolderMode && !isDialogOpened) {
                                    onMoreClicked()
                                }
                            }
                    )

                }
                if (isEditMode) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_copy),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable {
                                if (!isDialogOpened) {
                                    onCopyClicked()
                                }
                            }
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_move),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable {
                                if (!isDialogOpened) {
                                    onMoveClicked()
                                }
                            }
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable {
                                if (!isDialogOpened) {
                                    onDeleteClicked()
                                }
                            }
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.White
            ),
            navigationIcon = {
                if (isEditMode) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable {
                                if (!isDialogOpened) {
                                    onCloseClicked()
                                }
                            }
                            .width(18.dp)
                            .height(18.dp)
                    )
                } else if (stackSize > 1 || isSearchMode || isFilterMode) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable {
                                if (!isDialogOpened) {
                                    searchQuery = ""
                                    onBackClicked()
                                }
                            }
                    )
                }
            }
        )
        if (!isSdCardExist || isDirectory || isSearchMode) {
            if (isSearchMode.not() && stackSize < 1) {
                FileScreenNavigationBar(
                    titles = titles,
                    overflowCounter = overflowCounter,
                    onOverflow = onOverflow,
                    onBackToDirectory = { count ->
                        onBackToDirectory(count)
                    },
                    onHomeClicked = {
                        onHomeClicked()
                    }
                )
            }
            Divider(
                thickness = 2.dp,
                color = Color.Black
            )
        } else {
            FileScreenTab(
                selectedStorageType = selectedStorageType,
                onTabClicked = { index ->
                    onTabClicked(index)
                },
                isCreateFolderMode = isCreateFolderMode,
                isDialogOpened = isDialogOpened
            )
        }
    }
}