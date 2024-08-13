package com.example.domain.models

import androidx.annotation.DrawableRes
import com.example.domain.R

enum class FileType(@DrawableRes val icon: Int) {
    IMAGE(R.drawable.ic_image),
    EBOOK(R.drawable.ic_explore),
    AUDIO(R.drawable.ic_song),
    UNKNOWN(R.drawable.ic_document)
}

fun File.getType() : FileType {
    return when(name.substringAfter('.')) {
        "mp3", "3gp", "mp4", "m4a", "aac", "ts", "amr", "flac",
        "mid", "xmf", "mxmf", "rtttl", "rtx", "ota", "imy", "ogg", "mkv", "wav"-> FileType.AUDIO
        "jpg", "bmp", "gift", "png", "webp", "heif" -> FileType.IMAGE
        "epub" -> FileType.EBOOK
        else -> FileType.UNKNOWN
    }
}