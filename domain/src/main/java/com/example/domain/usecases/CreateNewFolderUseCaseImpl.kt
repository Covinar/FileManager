package com.example.domain.usecases

import com.example.domain.models.File
import com.example.domain.repositories.FilesRepository

class CreateNewFolderUseCaseImpl(
    private val filesRepository: FilesRepository
): CreateNewFolderUseCase {

    override fun invoke(path: String): File? {
        return filesRepository.createNewFolder(path)
    }

}