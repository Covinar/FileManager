package com.example.domain.models

enum class FilterType {
    DEFAULT, IMAGES, AUDIO, EBOOKS
}

private val imageForamts = setOf("jpg", "jpeg", "png", "gif", "bmp", "webp", "heif")
private val audioFormats = setOf("mp3", "3gp", "mp4", "m4a", "aac", "ts", "amr", "flac",
    "mid", "xmf", "mxmf", "rtttl", "rtx", "ota", "imy", "ogg", "mkv", "wav")
private val bookFormats = setOf("epub")

fun File.getFilterType(): FilterType {
    val fileName = name
    val lowercaseFileName = fileName.lowercase()
    return when {
        imageForamts.contains(lowercaseFileName.substringAfterLast('.')) -> FilterType.IMAGES
        audioFormats.contains(lowercaseFileName.substringAfterLast('.')) -> FilterType.AUDIO
        bookFormats.contains(lowercaseFileName.substringAfterLast('.')) -> FilterType.EBOOKS
        else -> FilterType.DEFAULT
    }
}
