package com.example.presentation.files

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.File
import com.example.domain.models.FilterType
import com.example.domain.usecases.CreateNewFolderUseCase
import com.example.domain.usecases.DeleteFileUseCase
import com.example.domain.usecases.FilterFilesUseCase
import com.example.domain.usecases.GetExternalStorageDirUseCase
import com.example.domain.usecases.GetFilesUseCase
import com.example.domain.usecases.SearchFilesUseCase
import com.example.presentation.R
import com.example.presentation.files.utils.SortType
import com.example.presentation.files.utils.SpeedLevel
import com.example.presentation.files.utils.StorageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FilesViewModel @Inject constructor(
    private val getFilesUseCase: GetFilesUseCase,
    private val getExternalStorageDirUseCase: GetExternalStorageDirUseCase,
    private val searchFilesUseCase: SearchFilesUseCase,
    private val filterFilesUseCase: FilterFilesUseCase,
    private val deleteFileUseCase: DeleteFileUseCase,
    private val createNewFolderUseCase: CreateNewFolderUseCase
) : ViewModel() {

    private val _event = Channel<Event>(capacity = Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    private var searchJob: Job? = null

    init {
        initDirectories()
    }

    fun onDirectoryClicked(title: String, path: String) {
        if (state.value.filterType == FilterType.DEFAULT) {
            getFiles(title, path)
        } else {
            _state.update { it.copy(isLoading = true) }
            viewModelScope.launch(Dispatchers.IO) {
                val files = filterFilesUseCase(path, state.value.filterType)
                withContext(Dispatchers.Main) {
                    _state.update {
                        val current = it.files.toMutableList().apply {
                            add(title to files)
                        }
                        it.copy(
                            files = ArrayDeque(current),
                            isLoading = false
                        )
                    }
                    sortContent()
                    _event.trySend(Event.ScrollToItem(0))
                }
            }
        }
    }

    fun deleteFile() {
        val selectedFiles = state.value.selectedFiles
        selectedFiles.forEach { file ->
            deleteFileUseCase(file)
        }
        val files = state.value.files.last().second.toMutableList()
        val title = state.value.files.last().first
        files.removeAll(selectedFiles)
        _state.update {
            val current = it.files.toMutableList().apply {
                removeLast()
                add(title to files)
            }
            it.copy(files = ArrayDeque(current))
        }
        sortContent()
    }

    fun getFiles(title: String, path: String) {
        val exist = state.value.directories.contains(path)
        if (exist) {
            _state.update {
                State(
                    directories = it.directories
                )
            }
        }
        _state.update {
            it.copy(
                isLoading = true
                )
        }
        viewModelScope.launch(Dispatchers.IO) {
            val files = getFilesUseCase(path)
            withContext(Dispatchers.Main) {
                _state.update {
                    val current = it.files.toMutableList().apply {
                        add(title to files)
                    }
                    it.copy(
                        files = ArrayDeque(current),
                        isLoading = false,
                        overflowCounter = 0
                    )
                }
                sortContent()
                _event.trySend(Event.ScrollToItem(0))
            }
        }
    }

    fun initDirectories() {
        viewModelScope.launch(Dispatchers.IO) {
            val directories = getExternalStorageDirUseCase()
            _state.update {
                it.copy(
                    directories = directories,
                    overflowCounter = 0
                )
            }
            getFiles(DEFAULT_TITLE, directories[0])
        }
    }

    fun filter(storageDirectory: StorageType, title: String, filterType: FilterType) {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true, files = ArrayDeque(listOf(it.files.first())))
            }
            withContext(Dispatchers.IO) {
                val storagePath =
                    when(storageDirectory) {
                        StorageType.PHONE -> { state.value.directories[0] }
                        StorageType.SD_CARD -> { state.value.directories[1] }
                    }
                val files = filterFilesUseCase(storagePath, filterType)
                val current = state.value.files.toMutableList().apply {
                    add(title to files)
                }
                withContext(Dispatchers.Main){
                    _state.update {
                        it.copy(
                            filterType = filterType,
                            files = ArrayDeque(current),
                            isLoading = false,
                            overflowCounter = 0
                        )
                    }
                    sortContent()
                }
            }
        }
    }

    fun filteredFilesTitle(filterType: FilterType): String {
        return when(filterType) {
            FilterType.IMAGES -> { IMAGES_TITLE }
            FilterType.AUDIO -> { AUDIO_TITLE }
            FilterType.EBOOKS -> { EBOOKS_TITLE }
            FilterType.DEFAULT -> { DEFAULT_TITLE }
        }
    }

    fun search(searchQuery: String) {
        if (searchQuery.isBlank()) return
        if (searchJob != null) {
            searchJob?.cancel()
            searchJob = null
        }
        searchJob = viewModelScope.launch {
            delay(2000)
            _state.update {
                it.copy(searchQuery = searchQuery, isLoading = true, files = ArrayDeque(listOf(it.files.first())))
            }
            withContext(Dispatchers.IO) {
                val files = searchFilesUseCase(searchQuery)
                withContext(Dispatchers.Main) {
                    val current = state.value.files.toMutableList().apply {
                        add("" to files)
                    }
                    _state.update {
                        it.copy(
                            files = ArrayDeque(current),
                            isLoading = false,
                            overflowCounter = 0
                        )
                    }
                    sortContent()
                }
            }
        }
    }

    fun getStorageIcon(path: String): Int {
        val dirs = state.value.directories
        return if (path.contains(dirs[0])) {
            R.drawable.ic_phone
        } else {
            R.drawable.ic_sd_card
        }
    }

    fun showMoreDialog() {
        _state.update {
            it.copy(showMoreDialog = state.value.showMoreDialog.not())
        }
    }

    fun showSortDialog() {
        _state.update {
            it.copy(showSortDialog = state.value.showSortDialog.not())
        }
    }

    fun changeSearchMode() {
        _state.update {
            it.copy(isSearchMode = state.value.isSearchMode.not())
        }
        if (state.value.isSearchMode.not()) {
            _state.update { it.copy(files = ArrayDeque(listOf(it.files.first())), isLoading = false) }
            searchJob?.cancel()
            searchJob = null
        }
    }

    fun changeFilterMode() {
        _state.update {
            it.copy(isFilterMode = state.value.isFilterMode.not())
        }
        _event.trySend(Event.ScrollToItem(0))
    }

    fun changeFilterDialogMode() {
        _state.update {
            it.copy(isFilterDialogMode = state.value.isFilterDialogMode.not())
        }
    }

    fun changeUnknownFileMode() {
        _state.update {
            it.copy(isUnknownFileMode = state.value.isUnknownFileMode.not())
        }
    }

    fun changeInfoClickedMode() {
        _state.update {
            it.copy(isInfoClicked = state.value.isInfoClicked.not())
        }
    }

    fun changeEditMode() {
        _state.update {
            it.copy(isEditMode = state.value.isEditMode.not())
        }
    }

    fun changeDeleteMode() {
        _state.update {
            it.copy(isDeleteMode = state.value.isDeleteMode.not())
        }
    }

    fun showDeletedItemsDialog() {
        _state.update {
            it.copy(showDeletedItemsDialog = state.value.showDeletedItemsDialog.not())
        }
    }

    fun onNavigationBarOverflow() {
        _state.update {
            it.copy(overflowCounter = state.value.overflowCounter + 1)
        }
    }

    fun clearSearch() {
        _state.update {
            it.copy(searchQuery = "")
        }
    }

    fun initFile(file: File) {
        _state.update {
            it.copy(currentFile = file)
        }
    }

    fun back() {
        if (state.value.files.size < 2) return
        val files = state.value.files.toMutableList().apply {
            removeLast()
        }
        _state.update {
            it.copy(files = ArrayDeque(files), overflowCounter = 0)
        }
        sortContent()
    }

    fun backToDirectory(count: Int) {
        val files = state.value.files.toMutableList()
        val range = files.lastIndex - count - 1
        repeat(range){
            files.removeLastOrNull()
        }
        _state.update {
            it.copy(files = ArrayDeque(files), overflowCounter = 0)
        }
        if (state.value.files.size == 1) {
            _state.update { it.copy(isFilterMode = false, filterType = FilterType.DEFAULT) }
        }
        sortContent()
    }

    fun selectFile(file: File) {
        val selectedFiles = state.value.selectedFiles.toMutableList()
        if (selectedFiles.contains(file)){
            selectedFiles.remove(file)
        } else {
            selectedFiles.add(file)
        }
        _state.update {
            it.copy(selectedFiles = selectedFiles)
        }
    }

    fun isFileSelected(file: File): Boolean {
        val selectedFiles = state.value.selectedFiles
        return selectedFiles.contains(file)
    }

    fun getFileStackTitles() : List<String> {
        val files = state.value.files
        val titles = mutableListOf<String>()
        files.forEach {
            titles.add(it.first)
        }
        titles.removeAt(0)
        return titles
    }

    fun changeSortType(sortType: SortType) {
        _state.update {
            it.copy(sortType = sortType)
        }
        sortContent()
    }

    fun isFileExists() {
        if (state.value.currentFile?.exists == false) {
            val files = state.value.files.last().second.toMutableList().apply {
                remove(state.value.currentFile)
            }
            val title = state.value.files.last().first
            _state.update {
                val current = it.files.toMutableList().apply {
                    removeLast()
                    add(title to files)
                }
                it.copy(files = ArrayDeque(current))
            }
        }
    }

    fun createNewFolder(name: String) {
        if (name.isBlank()) return
        val current = state.value.currentFiles.filter { it.isDirectory }.map { it.name }
        if (current.contains(name)) return
        val path = state.value.currentFiles.firstOrNull { it.isNewFile }?.path
        val newPath = path?.replaceAfterLast("/", name).orEmpty()
        val folder = createNewFolderUseCase(newPath)
        val files = state.value.files.last().second.toMutableList().apply {
            if (folder != null) {
                replaceAll {
                    if (it.isNewFile) {
                        folder
                    } else {
                        it
                    }
                }
            }
        }
        val title = state.value.files.last().first
        _state.update {
            val current = it.files.toMutableList().apply {
                removeLast()
                add(title to files)
            }
            it.copy(
                files = ArrayDeque(current)
            )
        }
        sortContent()
    }

    fun changeCreateFolderMode() {
        _state.update {
            it.copy(isCreateFileMode = state.value.isCreateFileMode.not())
        }
    }

    fun enableAudioMode(enable: Boolean) {
        _state.update {
            it.copy(isAudioDialogMode = enable)
        }
    }

    fun changeAudioPlayMode() {
        _state.update {
            it.copy(isAudioPaused = state.value.isAudioPaused.not())
        }
    }

    fun isServiceBound() {
        _state.update {
            it.copy(isBound = state.value.isBound.not())
        }
    }

    fun changeSpeedLevel() {
        var ordinal = state.value.speedLevel.ordinal + 1
        if (ordinal > 5) {
            ordinal = 0
        }
        _state.update {
            it.copy(speedLevel = SpeedLevel.entries[ordinal])
        }
    }

    fun changeSpeedLevelTo1() {
        _state.update {
            it.copy(speedLevel = SpeedLevel.X1)
        }
    }

    fun addEmptyFile() {
        var path = state.value.directories[state.value.storageType.ordinal]
        val filesStack = state.value.files
        filesStack.forEachIndexed { index, pair ->
            if (index != 0) {
                path += "/" + pair.first
            }
        }
        path += "/New File"
        val emptyFile = File("New Folder", path, System.currentTimeMillis(), true, 0, true)
        val files = state.value.files.last().second.toMutableList().apply {
            add(0, emptyFile)
        }
        val title = state.value.files.last().first
        _state.update {
            val current = it.files.toMutableList().apply {
                removeLast()
                add(title to files)
            }
            it.copy(
                files = ArrayDeque(current)
            )
        }
        viewModelScope.launch {
            delay(500)
            _event.send(Event.ScrollToItem(0))
        }
    }

    fun deleteEmptyFile() {
        val files = state.value.currentFiles.toMutableList()
        val title = state.value.files.last().first
        files.removeAt(0)
        _state.update {
            val current = it.files.toMutableList().apply {
                removeLast()
                add(title to files)
            }
            it.copy(files = ArrayDeque(current))
        }
    }

    private fun sortContent() {
        val sortType = state.value.sortType
        val title = state.value.files.last().first
        val files = state.value.files.last().second.toMutableList()
        when(sortType) {
            SortType.NAME_AZ -> {
                files.sortBy { it.name }
            }
            SortType.NAME_ZA -> {
                files.sortByDescending { it.name }
            }
            SortType.FROM_LARGEST_TO_SMALLEST -> {
                files.sortByDescending { it.size }
            }
            SortType.FROM_SMALLEST_TO_LARGEST -> {
                files.sortBy { it.size }
            }
            SortType.FROM_NEWEST_TO_OLDEST -> {
                files.sortByDescending { it.date }
            }
            SortType.FROM_OLDEST_TO_NEWEST -> {
                files.sortBy { it.date}
            }
        }
        val new = state.value.files.toMutableList().apply {
            removeLast()
            add(title to files)
        }
        _state.update {
            it.copy(
                files = ArrayDeque(new)
            )
        }
    }


    data class State(
        val files: ArrayDeque<Pair<String, List<File>>> = ArrayDeque(),
        val directories: List<String> = emptyList(),
        val currentFile: File? = null,
        val isLoading: Boolean = false,
        val isSearchMode: Boolean = false,
        val isFilterMode: Boolean = false,
        val isInfoClicked: Boolean = false,
        val isUnknownFileMode: Boolean = false,
        val isEditMode: Boolean = false,
        val isFilterDialogMode: Boolean = false,
        val isDeleteMode: Boolean = false,
        val title: String = DEFAULT_TITLE,
        val filterType: FilterType = FilterType.DEFAULT,
        val storageType: StorageType = StorageType.PHONE,
        val searchQuery: String = "",
        val showMoreDialog: Boolean = false,
        val showSortDialog: Boolean = false,
        val showDeletedItemsDialog: Boolean = false,
        val isCreateFileMode: Boolean = false,
        val overflowCounter: Int = 0,
        val selectedFiles: List<File> = emptyList(),
        val sortType: SortType = SortType.NAME_AZ,
        val isAudioDialogMode: Boolean = false,
        val isAudioPaused: Boolean = false,
        val isBound: Boolean = false,
        val speedLevel: SpeedLevel = SpeedLevel.X1
    ) {
        val currentFiles
            get() = files.lastOrNull()?.second ?: emptyList()

        val isFolderEmpty
            get() = files.lastOrNull()?.second?.isEmpty()  == true

        val isDialogOpened
            get() = isFilterDialogMode || showDeletedItemsDialog || showSortDialog || showMoreDialog || isAudioDialogMode

    }

    sealed class Event {
        data class ScrollToItem(val index: Int): Event()
    }

    companion object {
        const val DEFAULT_TITLE = "Files"
        const val IMAGES_TITLE = "Images"
        const val AUDIO_TITLE = "Audio"
        const val EBOOKS_TITLE = "E-Books"
    }
}