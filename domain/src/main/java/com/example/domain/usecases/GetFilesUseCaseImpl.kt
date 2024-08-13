package com.example.domain.usecases

import com.example.domain.models.File
import com.example.domain.models.FilterType
import com.example.domain.repositories.FilesRepository

class GetFilesUseCaseImpl(
    private val filesRepository: FilesRepository
): GetFilesUseCase {

    override fun invoke(path: String): List<File> {
        return filesRepository.getFiles(path)
    }

}