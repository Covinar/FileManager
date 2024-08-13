package com.example.domain.mappers

import com.example.domain.models.FileDomain
import java.io.File

fun File.toFile(isNewFile: Boolean = false) = FileDomain(name, path, lastModified(), isDirectory, sizeOfFile(path), isNewFile = isNewFile)

fun List<File>?.toFiles() = this?.map { it.toFile() } ?: emptyList()

private fun sizeOfFile(path: String): Long {
    val file = File(path)
    val sum = if (file.isDirectory) {
        sumOfDirectory(file)
    } else {
        file.length()
    }
    return sum
}

private fun sumOfDirectory(directory: File): Long {
    var sum = 0L
    val files = directory.listFiles()
    files?.forEach { file ->
        sum += if (file.isDirectory) {
            sumOfDirectory(file)
        } else {
            file.length()
        }
    }
    return sum
}