package com.example.domain.usecases

import com.example.domain.models.FileDomain
import com.example.domain.repositories.FilesRepository

class GetFileUseCaseImpl(
    private val filesRepository: FilesRepository
): GetFileUseCase {

    override fun invoke(path: String): FileDomain {
        return filesRepository.getFile(path)
    }

}