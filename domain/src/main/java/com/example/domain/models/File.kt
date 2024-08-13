package com.example.domain.models

typealias FileDomain = File

data class File(
    val name: String,
    val path: String,
    val date: Long,
    val isDirectory: Boolean,
    val size: Long,
    val isNewFile: Boolean
) {
    val formattedSize: String
        get() {
            val kilobytes = size.toFloat() / 1024
            val megabytes = kilobytes / 1024
            return if (megabytes >= 1024) {
                val gigabytes = megabytes / 1024
                String.format("%.2f GB", gigabytes)
            } else {
                val string = String.format("%.2f", megabytes)
                if (string.replace(",", ".").toDouble() == 0.0) {
                    "0 MB"
                } else {
                    "$string MB"
                }
            }
        }

    val exists: Boolean
        get() = java.io.File(path).exists()

}
