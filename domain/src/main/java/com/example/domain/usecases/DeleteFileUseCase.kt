package com.example.domain.usecases

import com.example.domain.models.File

interface DeleteFileUseCase {

    operator fun invoke(file: File)

}