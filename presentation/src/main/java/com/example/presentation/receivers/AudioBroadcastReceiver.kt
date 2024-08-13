package com.example.presentation.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.presentation.services.AudioService

class AudioBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            context?.sendBroadcast(Intent(AudioService.BROADCAST_INTENT_FILTER).apply {
                putExtra(AudioService.AUDIO_KEY, intent.action)
            })
        }
    }

}