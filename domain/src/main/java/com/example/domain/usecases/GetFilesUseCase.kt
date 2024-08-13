package com.example.domain.usecases

import com.example.domain.models.File

interface GetFilesUseCase {

    operator fun invoke(path: String) : List<File>

}