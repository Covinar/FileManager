package com.example.data.repositories

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.domain.mappers.toFile
import com.example.domain.models.FileDomain
import com.example.domain.models.FilterType
import com.example.domain.models.getFilterType
import com.example.domain.repositories.FilesRepository
import java.io.File


class FilesRepositoryImpl(
    private val context: Context
) : FilesRepository {

    override fun getFiles(path: String): List<FileDomain> {
        val files = File(path).listFiles()?.filterNotNull()
        val filteredFiles = mutableListOf<FileDomain>()
        files?.forEach { file ->
            filteredFiles.add(file.toFile())
        }
        return filteredFiles
    }

    override fun getFile(path: String): FileDomain {
        val file = File(path)
        return file.toFile()
    }

    override fun delete(file: FileDomain) {
        val item = File(file.path)
        item.deleteRecursively()
    }

    override fun createNewFolder(path: String): FileDomain {
        val file = File(path)
        file.mkdir()
        return  File(path).toFile()
    }

    override fun getExternalStorageDirectories(): List<String> {
        val externalPaths = mutableListOf<String>()
        val allExternalFilesDirs = ContextCompat.getExternalFilesDirs(context, null)
        for (filesDir in allExternalFilesDirs) {
            if (filesDir != null) {
                val nameSubPos = filesDir.absolutePath.lastIndexOf("/Android/data")
                if (nameSubPos > 0) {
                    val filesDirName = filesDir.absolutePath.substring(0, nameSubPos)
                    externalPaths.add(filesDirName)
                }
            }
        }
        return externalPaths
    }

    override fun search(searchQuery: String): List<FileDomain> {
        val searchFiles = mutableListOf<FileDomain>()
        val dirs = getExternalStorageDirectories()
        dirs.forEach {
            searchFiles.addAll(runSearch(it, searchQuery))
        }
        return searchFiles
    }

    private fun runSearch(path: String, searchQuery: String): List<FileDomain> {
        val searchFiles = mutableListOf<FileDomain>()
        val files = getFiles(path)
        files.forEach {  file ->
            if (file.isDirectory) {
                searchFiles.addAll(runSearch(file.path, searchQuery))
            }
            if (file.name.startsWith(searchQuery, true)) {
                searchFiles.add(file)
            }
        }
        return searchFiles
    }

    override fun filterFiles(directory: String, filterType: FilterType): List<FileDomain> {
        val file = File(directory)
        val files = file.listFiles()
        val filteredFiles = mutableListOf<FileDomain>()
        files?.forEach { file ->
            if (file.isDirectory) {
                if (isFiletypeExist(file.absolutePath, filterType)) {
                    filteredFiles.add(file.toFile())
                }
            } else {
                if (file.toFile().getFilterType() == filterType) {
                    filteredFiles.add(file.toFile())
                }
            }
        }
        return filteredFiles
    }

    private fun isFiletypeExist(path: String, filterType: FilterType): Boolean {
        val file = File(path)
        val files = file.listFiles()
        files?.filter { it.isDirectory.not() }?.forEach { file ->
            if (file.toFile().getFilterType() == filterType) {
                return true
            }
        }
        files?.filter { it.isDirectory }?.forEach { file ->
            return isFiletypeExist(file.path, filterType)
        }
        return false
    }



}