package com.example.domain.repositories

import com.example.domain.models.File
import com.example.domain.models.FilterType

interface FilesRepository {

    fun getFiles(path: String): List<File>

    fun getExternalStorageDirectories(): List<String>

    fun search(searchQuery: String): List<File>

    fun getFile(path: String): File

    fun delete(file: File)

    fun filterFiles(root: String, filterType: FilterType): List<File>

    fun createNewFolder(path: String): File?

}