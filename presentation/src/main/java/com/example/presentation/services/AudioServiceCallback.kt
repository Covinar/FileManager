package com.example.presentation.services

import com.example.domain.models.File

interface AudioServiceCallback {

    fun onStop()

    fun onPause()

    fun onTrackChanged(music: File)

}