package com.example.presentation.services

import android.app.Notification
import android.app.Notification.MediaStyle
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.media.session.MediaSession
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.domain.models.File
import com.example.presentation.R
import com.example.presentation.files.utils.SpeedLevel
import com.example.presentation.receivers.AudioBroadcastReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AudioService: Service() {

    private val binder = LocalBinder()
    private var mediaPlayer: MediaPlayer? = null
    private var audioServiceServiceCallback: AudioServiceCallback? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val firstFlow = MutableStateFlow(0.0)
    private val secondFlow = flow {
        while (true) {
            delay(1000)
            mediaPlayer?.let {
                startForeground(NOTIFICATION_ID, createCustomNotification(fileName))
                emit(it.currentPosition.toDouble() / it.duration)
            }
        }
    }

    val audioFlow = merge(firstFlow, secondFlow)
    private var seekJob: Job? = null

    private val audioReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.getStringExtra(AUDIO_KEY)
            when(action) {
                ACTION_BACKWARD -> { seekTo15Seconds(false) }
                ACTION_PAUSE -> { pause() }
                ACTION_FORWARD -> { seekTo15Seconds(true) }
                ACTION_CLOSE -> { stop() }
            }
        }

    }

    private var fileName = ""

    private val playPauseDrawable: Int
        get() {
            return if (mediaPlayer?.isPlaying == true) {
                android.R.drawable.ic_media_play
            } else {
                android.R.drawable.ic_media_pause

            }
        }

    private val playPauseCustomDrawable: Int
        get() {
            return if (mediaPlayer?.isPlaying == true) {
                R.drawable.ic_stop
            } else {
                R.drawable.ic_play

            }
        }

    private val pendingIntentBackward by lazy {
        PendingIntent.getBroadcast(
            applicationContext,
            0,
            Intent(this, AudioBroadcastReceiver::class.java).apply {
                action = ACTION_BACKWARD
            },
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    private val pendingIntentPause by lazy {
        PendingIntent.getBroadcast(
            applicationContext,
            0,
            Intent(this, AudioBroadcastReceiver::class.java).apply {
                action = ACTION_PAUSE
            },
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    private val pendingIntentForward by lazy {
        PendingIntent.getBroadcast(
            applicationContext,
            0,
            Intent(this, AudioBroadcastReceiver::class.java).apply {
                action = ACTION_FORWARD
            },
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    private val pendingIntentClose by lazy {
        PendingIntent.getBroadcast(
            applicationContext,
            0,
            Intent(this, AudioBroadcastReceiver::class.java).apply {
                action = ACTION_CLOSE
            },
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun getDuration(): Int {
        return mediaPlayer?.duration ?: 0
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(audioReceiver, IntentFilter(BROADCAST_INTENT_FILTER),
                RECEIVER_EXPORTED)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(audioReceiver)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_REDELIVER_INTENT
    }

    inner class LocalBinder : Binder() {
        fun getService(): AudioService = this@AudioService
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder

    }

    private fun createNotificationChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ONE, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun createCustomNotification(name: String): Notification {
        val notificationLayout = RemoteViews(packageName, R.layout.custom_notification_layout)
        val duration = mediaPlayer?.duration ?: 1
        val currentPosition = mediaPlayer?.currentPosition ?: 0
        val progress = currentPosition.toDouble() / duration * 100
        notificationLayout.setTextViewText(R.id.tv_filename, name)
        notificationLayout.setProgressBar(R.id.audio_progress_bar, 100, progress.toInt(), false)
        notificationLayout.setImageViewResource(R.id.iv_backward, R.drawable.ic_backward)
        notificationLayout.setImageViewResource(R.id.iv_pause, playPauseCustomDrawable)
        notificationLayout.setImageViewResource(R.id.iv_forward, R.drawable.ic_forward)
        notificationLayout.setImageViewResource(R.id.iv_close, R.drawable.ic_close)
        setListeners(notificationLayout)
        val customNotification = NotificationCompat.Builder(this, CHANNEL_ONE)
            .setContentTitle(name)
            .setSmallIcon(R.drawable.ic_audio)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(notificationLayout)
            .setSilent(true)
            .build()
        return customNotification
    }

    private fun setListeners(remoteViews: RemoteViews) {
        remoteViews.setOnClickPendingIntent(R.id.iv_backward, pendingIntentBackward)
        remoteViews.setOnClickPendingIntent(R.id.iv_pause, pendingIntentPause)
        remoteViews.setOnClickPendingIntent(R.id.iv_forward, pendingIntentForward)
        remoteViews.setOnClickPendingIntent(R.id.iv_close, pendingIntentClose)
    }

    private fun createNotification(name: String) = NotificationCompat.Builder(this, CHANNEL_ONE)
        .setSilent(true)
        .setContentTitle(name)
        .setContentText("")
        .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
        .setSmallIcon(R.drawable.ic_stop)
        .addAction(R.drawable.ic_backward, "Backward", pendingIntentBackward)
        .addAction(playPauseDrawable, "Pause", pendingIntentPause)
        .addAction(R.drawable.ic_forward, "Forward", pendingIntentForward)
        .addAction(R.drawable.ic_close, "Close", pendingIntentClose)
        .setStyle(androidx.media.app.NotificationCompat.MediaStyle()
            .setShowActionsInCompactView(0, 1, 2, 3)
            .setMediaSession(MediaSessionCompat(applicationContext, "Session").sessionToken)
        )
        .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
        .build()

    fun play(file: File) {
        fileName = file.name
        startForeground(NOTIFICATION_ID, createCustomNotification(file.name))
        audioServiceServiceCallback?.onTrackChanged(file)
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(file.path)
            prepare()
            start()
        }
    }

    fun stop() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        audioServiceServiceCallback?.onStop()
    }

    fun pause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                audioServiceServiceCallback?.onPause()
                it.pause()
            } else {
                audioServiceServiceCallback?.onPause()
                it.start()
            }
        }
        startForeground(NOTIFICATION_ID, createCustomNotification(fileName))
    }

    fun seekTo15Seconds(isForward: Boolean) {
        var current = mediaPlayer?.currentPosition ?: 0
        if (isForward) {
            current += 1500
        } else {
            current -= 1500
        }
        mediaPlayer?.seekTo(current)
    }

    fun toSpeed(speedLevel: SpeedLevel) {
        mediaPlayer?.playbackParams = PlaybackParams().apply {
            speed = speedLevel.level
        }
    }

    fun seekTo(position: Float) {
        if (seekJob != null) {
            seekJob?.cancel()
            seekJob = null
        }
        seekJob = coroutineScope.launch {
            delay(500)
            val dur = mediaPlayer?.duration ?: 0
            mediaPlayer?.seekTo((position * dur).toInt())
            firstFlow.emit(position.toDouble())
        }
    }



    fun setupServiceCallback(callback: AudioServiceCallback?) {
        audioServiceServiceCallback = callback
    }

    companion object {
        const val NOTIFICATION_ID = 123
        const val CHANNEL_ONE = "channel_one"
        const val CHANNEL_NAME = "audio"

        private const val ACTION_BACKWARD = "action_backward"
        private const val ACTION_PAUSE = "action_pause"
        private const val ACTION_FORWARD = "action_forward"
        private const val ACTION_CLOSE = "action_close"

        const val BROADCAST_INTENT_FILTER = "broadcast_intent_filter"
        const val AUDIO_KEY = "audio_key"
    }

}

