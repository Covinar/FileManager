package com.example.presentation.files.utils

fun toStringDuration(duration: Int): String {
    val minutes: Int = (duration / 1000) / 60
    val seconds: Int = (duration / 1000) % 60
    var string = if (minutes < 10) "0$minutes:" else "$minutes:"
    string = if (seconds < 10) string + "0" + seconds.toString() else string + seconds.toString()
    return string
}