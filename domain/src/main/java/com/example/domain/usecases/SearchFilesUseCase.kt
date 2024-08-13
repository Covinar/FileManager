package com.example.domain.usecases

import com.example.domain.models.File

interface SearchFilesUseCase {

    operator fun invoke(searchQuery: String) : List<File>

}