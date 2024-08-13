package com.example.domain.usecases

import com.example.domain.models.File
import com.example.domain.repositories.FilesRepository

class SearchFilesUseCaseImpl(
    private val filesRepository: FilesRepository
): SearchFilesUseCase {

    override fun invoke(searchQuery: String): List<File> {
        return filesRepository.search(searchQuery)
    }

}