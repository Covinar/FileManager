package com.example.domain.usecases

import com.example.domain.models.File
import com.example.domain.models.FilterType

interface FilterFilesUseCase {

    operator fun invoke(directory: String, filterType: FilterType): List<File>

}