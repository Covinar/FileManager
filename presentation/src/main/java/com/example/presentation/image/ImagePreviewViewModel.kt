package com.example.presentation.image

import androidx.lifecycle.ViewModel
import com.example.domain.models.File
import com.example.domain.usecases.DeleteFileUseCase
import com.example.domain.usecases.GetFileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ImagePreviewViewModel @Inject constructor(
    private val getFileUseCase: GetFileUseCase,
    private val deleteFileUseCase: DeleteFileUseCase
): ViewModel() {

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    fun infoClicked() {
        _state.update {
            it.copy(isInfoClicked = state.value.isInfoClicked.not())
        }
    }

    fun getFile(path: String) {
        val file = getFileUseCase(path)
        _state.update {
            it.copy(
                file = file
            )
        }
    }

    fun changeDeleteMode() {
        _state.update {
            it.copy(isDeleteMode = state.value.isDeleteMode.not())
        }
    }

    fun zoom(increase: Boolean) {
        val coefficient = if (increase) 0.25F else -0.25F
        _state.update {
            it.copy(zoom = it.zoom + coefficient)
        }
    }

    fun deleteFile() {
        val file = state.value.file
        if (file != null) {
            deleteFileUseCase(file)
        }
        _state.update {
            it.copy(file = null)
        }
    }

    data class State(
        val isInfoClicked: Boolean = false,
        val file: File? = null,
        val isDeleteMode: Boolean = false,
        val zoom: Float = 1F
    )
}