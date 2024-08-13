package com.example.domain.usecases

import com.example.domain.models.FileDomain

interface GetFileUseCase {

    operator fun invoke(path: String) : FileDomain

}