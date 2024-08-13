package com.example.domain.usecases

import com.example.domain.models.File
import com.example.domain.repositories.FilesRepository

class DeleteFileUseCaseImpl(
    private val filesRepository: FilesRepository
) : DeleteFileUseCase {

    override fun invoke(file: File) {
        filesRepository.delete(file)
    }

}