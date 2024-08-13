package com.example.presentation.files.models

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.domain.models.File
import com.example.presentation.R
import com.example.domain.models.getType
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Stable
@Composable
fun FileItem(
    file: File,
    isLast: Boolean,
    isSearchMode: Boolean,
    isSelected: Boolean,
    isEditMode: Boolean,
    storageIconId: Int,
    onClicked: () -> Unit,
    isCreateFolderMode: Boolean,
    isDialogOpened: Boolean,
    onCreateNewFolder: (String) -> Unit
) {

    var name by remember{ mutableStateOf("") }

    val painter = if (file.isDirectory) {
        R.drawable.ic_folder
    } else {
        file.getType().icon
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable {
                if (!isCreateFolderMode && !isDialogOpened) {
                    onClicked()
                }
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isEditMode) {
            if (isSelected) {
                Image(
                    painter = painterResource(id = R.drawable.ic_checked),
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp)
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_unchecked),
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp)
                )
            }
        } else {
            Icon(
                painter = painterResource(painter),
                contentDescription = null,
                modifier = Modifier.padding(8.dp)
            )
        }
        Column(Modifier.fillMaxWidth()) {
            if (file.isNewFile) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = name,
                    textStyle = TextStyle(fontWeight = FontWeight.Bold),
                    onValueChange = {
                        name = it
                    },
                    placeholder = {
                        Text(text = stringResource(id = R.string.folder_name))
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    keyboardActions = KeyboardActions {
                        onCreateNewFolder(name)
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )
            } else {
                Row {
                    Column(
                        modifier = Modifier.weight(1F)
                    ) {
                        Text(
                            text = file.name,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Row(
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Text(
                                text = file.formattedSize
                            )
                            MyCircle()
                            Text(
                                text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(
                                    Date(file.date)
                                )
                            )
                        }
                    }
                    if (file.isDirectory) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow),
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(end = 16.dp)
                        )
                    } else if (isSearchMode) {
                        Icon(
                            painter = painterResource(id = storageIconId),
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(end = 16.dp)
                        )
                    }
                }
            }
            if (!isLast) {
                Divider(
                    thickness = 1.dp,
                    color = Color.Gray
                )
            }
        }


    }

}

@Composable
fun MyCircle(){
    Canvas(
        modifier = Modifier
            .size(20.dp)
            .padding(8.dp),
        onDraw = {
            drawCircle(color = Color.Black)
        })
}