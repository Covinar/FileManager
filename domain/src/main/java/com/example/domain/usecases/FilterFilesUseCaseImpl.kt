package com.example.domain.usecases

import com.example.domain.models.File
import com.example.domain.models.FilterType
import com.example.domain.repositories.FilesRepository

class FilterFilesUseCaseImpl(
    private val getFilesRepository: FilesRepository
): FilterFilesUseCase {

    override fun invoke(directory: String, filterType: FilterType): List<File> {
        return getFilesRepository.filterFiles(directory, filterType)
    }

}