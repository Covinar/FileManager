package com.example.presentation.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

class SDCardBroadcastReceiver(
    private val initDirectories: () -> Unit
): BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        initDirectories()
    }

    companion object {
        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_MEDIA_MOUNTED)
            addAction(Intent.ACTION_MEDIA_EJECT)
            addAction(Intent.ACTION_MEDIA_UNMOUNTED)
            addAction(Intent.ACTION_MEDIA_REMOVED)
            addAction(Intent.ACTION_MEDIA_BAD_REMOVAL)
            addDataScheme("file")
        }
    }

}