package com.example.presentation.files

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.models.File
import com.example.domain.models.FileType
import com.example.domain.models.getType
import com.example.presentation.files.FilesViewModel.Companion.DEFAULT_TITLE
import com.example.presentation.files.components.EmptyFileScreen
import com.example.presentation.files.components.Files
import com.example.presentation.files.components.LoadingScreen
import com.example.presentation.files.components.TopBar
import com.example.presentation.files.models.AudioDialog
import com.example.presentation.files.models.DeleteDialog
import com.example.presentation.files.models.DeletedItemsDialog
import com.example.presentation.files.models.DialogFilter
import com.example.presentation.files.models.MoreOptionsDialog
import com.example.presentation.files.models.SortContentDialog
import com.example.presentation.files.models.UnknownFileDialog
import com.example.presentation.files.utils.StorageType
import com.example.presentation.image.FileDetails
import com.example.presentation.main.SDCardBroadcastReceiver
import com.example.presentation.services.AudioService
import com.example.presentation.services.AudioServiceCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilesScreen(
    viewModel: FilesViewModel = hiltViewModel(),
    onImageClicked: (path: String) -> Unit
) {

    val state = viewModel.state.collectAsState()
    val filesListState = rememberLazyListState()
    var selectedStorageType by remember { mutableStateOf(StorageType.PHONE) }
    val context = LocalContext.current
    var mService: AudioService? by remember {
        mutableStateOf(null)
    }
    var audioDuration by remember {
        mutableStateOf(0)
    }
    val audioCurrentPosition = remember {
        mutableDoubleStateOf(0.0)
    }
    val coroutineScope = rememberCoroutineScope()

    val audioServiceCallback = object : AudioServiceCallback {
        override fun onStop() {
            viewModel.enableAudioMode(false)
        }

        override fun onPause() {
            viewModel.changeAudioPlayMode()
        }

        override fun onTrackChanged(music: File) {
            viewModel.enableAudioMode(true)
            viewModel.initFile(music)
        }

    }

    val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as AudioService.LocalBinder
            mService = binder.getService()
            viewModel.isServiceBound()
            mService?.setupServiceCallback(audioServiceCallback)
            state.value.currentFile?.let {file ->
                mService?.play(file)
                audioDuration = mService?.getDuration() ?: 0
            }
            mService?.audioFlow
                ?.flowOn(Dispatchers.Default)
                ?.onEach {
                    audioCurrentPosition.doubleValue = it
                }
                ?.launchIn(coroutineScope)
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mService?.setupServiceCallback(null)
            mService = null
            viewModel.isServiceBound()
        }
    }


    LaunchedEffect(key1 = "files_screen") {
        viewModel.isFileExists()
        viewModel.event.collectLatest { event ->
            when (event) {
                is FilesViewModel.Event.ScrollToItem -> {
                    filesListState.scrollToItem(event.index)
                }
            }
        }
    }

    DisposableEffect(key1 = "files_screen_effect") {
        val receiver = SDCardBroadcastReceiver(
            initDirectories = {
                viewModel.initDirectories()
            }
        )
        context.registerReceiver(receiver, SDCardBroadcastReceiver.intentFilter)
        onDispose {
            context.unregisterReceiver(receiver)
        }
    }

    BackHandler(state.value.isEditMode || state.value.files.size > 1 || state.value.isSearchMode || state.value.isFilterMode || state.value.isCreateFileMode) {
        if (state.value.isEditMode) {
            viewModel.changeEditMode()
        } else if (state.value.files.size > 1 || state.value.isSearchMode || state.value.isFilterMode) {
            if (state.value.isSearchMode) {
                viewModel.changeSearchMode()
                viewModel.clearSearch()
            } else if (state.value.isFilterMode) {
                if (state.value.files.size == 2) {
                    viewModel.changeFilterMode()
                }
                viewModel.back()
            } else {
                viewModel.back()
            }
        }
        if (state.value.isCreateFileMode) {
            viewModel.changeCreateFolderMode()
            viewModel.deleteEmptyFile()
        }
    }

    Scaffold(
        topBar = {
            state.value.files.lastOrNull()?.let {
                TopBar(
                    selectedStorageType = selectedStorageType,
                    isSdCardExist = state.value.directories.size > 1,
                    onTabClicked = { index ->
                        viewModel.getFiles(DEFAULT_TITLE, state.value.directories[index])
                        selectedStorageType = StorageType.values()[index]
                    },
                    fileName = state.value.files.lastOrNull()?.first ?: DEFAULT_TITLE,
                    stackSize = state.value.files.size,
                    onBackClicked = {
                        if (state.value.isSearchMode) {
                            viewModel.changeSearchMode()
                            viewModel.clearSearch()
                        } else if (state.value.isFilterMode) {
                            if (state.value.files.size == 2) {
                                viewModel.changeFilterMode()
                            }
                            viewModel.back()
                        } else {
                            viewModel.back()
                        }
                    },
                    isDirectory = state.value.files.size > 1,
                    isSearchMode = state.value.isSearchMode,
                    onSearchClicked = {
                        viewModel.changeSearchMode()
                    },
                    onSearchQuery = {
                        viewModel.search(it)
                    },
                    onCancelClicked = {
                        viewModel.clearSearch()
                    },
                    onFilterClicked = {
                        viewModel.changeFilterDialogMode()
                    },
                    isFilterMode = state.value.isFilterMode,
                    isFilterDialogMode = state.value.isFilterDialogMode,
                    onMoreClicked = {
                        viewModel.showMoreDialog()
                    },
                    titles = viewModel.getFileStackTitles(),
                    overflowCounter = state.value.overflowCounter,
                    onOverflow = {
                        viewModel.onNavigationBarOverflow()
                    },
                    onBackToDirectory = { count ->
                        viewModel.backToDirectory(count)
                    },
                    onHomeClicked = {
                        viewModel.backToDirectory(-1)
                    },
                    selectedItemCount = state.value.selectedFiles.size,
                    isEditMode = state.value.isEditMode,
                    onCloseClicked = {
                        viewModel.changeEditMode()
                    },
                    onCopyClicked = {},
                    onMoveClicked = {},
                    onDeleteClicked = {
                        viewModel.changeDeleteMode()
                    },
                    isCreateFolderMode = state.value.isCreateFileMode,
                    isDialogOpened = state.value.isDialogOpened
                )
            }
        },
        containerColor = Color.White
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (state.value.isLoading) {
                LoadingScreen(
                    isSearchMode = state.value.isSearchMode,
                    isFilterMode = state.value.isFilterMode
                )
            } else if (state.value.isFolderEmpty) {
                EmptyFileScreen()
            } else {
                Files(
                    files = state.value.currentFiles,
                    modifier = Modifier.padding(it),
                    listState = filesListState,
                    isSearchMode = state.value.isSearchMode,
                    getStorageIconId = viewModel::getStorageIcon,
                    onItemClicked = { file ->
                        if (!state.value.isUnknownFileMode || !state.value.isInfoClicked) {
                            if (state.value.isEditMode) {
                                viewModel.selectFile(file)
                            } else {
                                if (file.isDirectory) {
                                    viewModel.onDirectoryClicked(file.name, file.path)
                                } else {
                                    when (file.getType()) {
                                        FileType.IMAGE -> {
                                            onImageClicked(file.path)
                                            viewModel.initFile(file)
                                        }

                                        FileType.EBOOK -> TODO()
                                        FileType.AUDIO -> {
                                            viewModel.initFile(file)
                                            viewModel.enableAudioMode(true)
                                            if (state.value.isBound) {
                                                mService?.play(file)
                                            } else {
                                                val intent = Intent(context, AudioService::class.java)
                                                context.bindService(intent, connection, BIND_AUTO_CREATE)
                                            }

                                            /*if (mediaPlayer.isPlaying) {
                                                mediaPlayer.stop()
                                                mediaPlayer.release()
                                            }
                                            mediaPlayer = MediaPlayer().apply {
                                                setDataSource(file.path)
                                                prepare()
                                                start()
                                            }*/
                                        }
                                        FileType.UNKNOWN -> viewModel.changeUnknownFileMode()
                                    }
                                }
                            }
                        }
                    },
                    isEditMode = state.value.isEditMode,
                    isSelected = { file ->
                        viewModel.isFileSelected(file)
                    },
                    onCreateNewFolder = { name ->
                        viewModel.createNewFolder(name)
                        viewModel.changeCreateFolderMode()
                    },
                    isCreateFolderMode = state.value.isCreateFileMode,
                    isDialogOpened = state.value.isDialogOpened
                )
            }
            if (state.value.isFilterDialogMode) {
                DialogFilter(
                    modifier = Modifier
                        .padding(it)
                        .align(Alignment.BottomCenter),
                    onCancelClicked = {
                        viewModel.changeFilterDialogMode()
                    },
                    onButtonClicked = { filterType ->
                        viewModel.changeFilterMode()
                        viewModel.changeFilterDialogMode()
                        viewModel.filter(
                            selectedStorageType,
                            viewModel.filteredFilesTitle(filterType),
                            filterType
                        )
                    }
                )
            }
            if (state.value.isUnknownFileMode) {
                state.value.currentFile?.name?.let { name ->
                    UnknownFileDialog(
                        modifier = Modifier
                            .padding(it)
                            .align(Alignment.BottomCenter),
                        title = name,
                        onCloseClicked = {
                            viewModel.changeUnknownFileMode()
                        },
                        onInfoClicked = {
                            viewModel.changeInfoClickedMode()
                            viewModel.changeUnknownFileMode()
                        }
                    )
                }
            }
            if (state.value.isInfoClicked) {
                state.value.currentFile?.let { file ->
                    FileDetails(
                        modifier = Modifier
                            .padding(it)
                            .align(Alignment.BottomCenter),
                        file = file,
                        onCloseClicked = {
                            viewModel.changeInfoClickedMode()
                            viewModel.enableAudioMode(true)
                        })
                }
            }
            if (state.value.showMoreDialog) {
                MoreOptionsDialog(
                    modifier = Modifier
                        .padding(it)
                        .align(Alignment.BottomCenter),
                    onCancelClicked = {
                        viewModel.showMoreDialog()
                    },
                    onCreateClicked = {
                        viewModel.showMoreDialog()
                        viewModel.addEmptyFile()
                        viewModel.changeCreateFolderMode()
                    },
                    onEditClicked = {
                        viewModel.showMoreDialog()
                        viewModel.changeEditMode()
                    },
                    onSortClicked = {
                        viewModel.showMoreDialog()
                        viewModel.showSortDialog()
                    }
                )
            }
            if (state.value.showSortDialog) {
                SortContentDialog(
                    modifier = Modifier
                        .padding(it)
                        .align(Alignment.BottomCenter),
                    onCloseClicked = {
                        viewModel.showSortDialog()
                    },
                    onSortTypeClicked = { sortType ->
                        viewModel.changeSortType(sortType)
                    }
                )
            }
            if (state.value.isDeleteMode) {
                DeleteDialog(
                    modifier = Modifier
                        .padding(it)
                        .align(Alignment.BottomCenter),
                    selectedFilesCount = state.value.selectedFiles.size,
                    onBackClicked = {
                        viewModel.changeDeleteMode()
                        if (state.value.currentFile?.getType() == FileType.AUDIO) {
                            viewModel.enableAudioMode(true)
                        }
                    },
                    onDeleteClicked = {
                        viewModel.deleteFile()
                        viewModel.changeDeleteMode()
                        viewModel.showDeletedItemsDialog()
                    }
                )
            }
            if (state.value.showDeletedItemsDialog) {
                DeletedItemsDialog(
                    modifier = Modifier
                        .padding(it)
                        .align(Alignment.BottomCenter),
                    selectedFilesCount = state.value.selectedFiles.size,
                    onCloseClicked = {
                        viewModel.showDeletedItemsDialog()
                    }
                )
            }
            if (state.value.isAudioDialogMode) {
                state.value.currentFile?.let { file ->
                    AudioDialog(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        file = file,
                        speedLevel = state.value.speedLevel,
                        isAudioPaused = state.value.isAudioPaused,
                        onSpeedClicked = {
                            viewModel.changeSpeedLevel()
                            mService?.toSpeed(state.value.speedLevel)
                        },
                        onCloseClicked = {
                            viewModel.enableAudioMode(false)
                            viewModel.changeSpeedLevelTo1()
                            mService?.stop()
                        },
                        onInfoClicked = {
                            viewModel.changeInfoClickedMode()
                            viewModel.enableAudioMode(false)
                        },
                        duration = mService?.getDuration() ?: 0,
                        currentPosition = audioCurrentPosition,
                        onPauseClicked = {
                            mService?.pause()
                        },
                        onSliderPositionChanged = { position ->
                            mService?.seekTo(position)
                        },
                        onBackwardClicked = {
                            mService?.seekTo15Seconds(false)
                        },
                        onForwardClicked = {
                            mService?.seekTo15Seconds(true)
                        },
                        onBackToStartClicked = {
                            mService?.seekTo(0.0F)
                        },
                        onDeleteClicked = {
                            viewModel.enableAudioMode(false)
                            viewModel.changeDeleteMode()
                            mService?.pause()
                        }
                    )
                }
            }
        }
    }
}

