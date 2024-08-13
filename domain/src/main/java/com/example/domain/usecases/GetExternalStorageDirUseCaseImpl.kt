package com.example.domain.usecases

import com.example.domain.repositories.FilesRepository

class GetExternalStorageDirUseCaseImpl(
    private val filesRepository: FilesRepository
) : GetExternalStorageDirUseCase {

    override fun invoke(): List<String> {
        return filesRepository.getExternalStorageDirectories()
    }

}