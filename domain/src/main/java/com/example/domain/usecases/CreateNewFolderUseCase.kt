package com.example.domain.usecases

import com.example.domain.models.File

interface CreateNewFolderUseCase {

    operator fun invoke(path: String): File?

}