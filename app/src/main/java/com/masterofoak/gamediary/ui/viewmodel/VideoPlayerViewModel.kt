package com.masterofoak.gamediary.ui.viewmodel

import android.app.Application
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer

@UnstableApi
class VideoPlayerViewModel(application: Application) : AndroidViewModel(application) {
    
    private val fastBackIncrementMs: Long = 10000L
    private val fastForwardIncrementMs: Long = 10000L
    private var currentVideoUrl: String? = null
    val exoPlayer by lazy {
        ExoPlayer.Builder(application).apply {
            setSeekBackIncrementMs(fastBackIncrementMs)
            setSeekForwardIncrementMs(fastForwardIncrementMs)
        }.build()
    }
    
    fun setVideoUrl(videoUri: String) {
        if (currentVideoUrl != videoUri) {
            currentVideoUrl = videoUri
            exoPlayer.setMediaItem(MediaItem.fromUri(videoUri.toUri()))
            exoPlayer.prepare()
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        exoPlayer.release()
    }
}

//@androidx.annotation.OptIn(UnstableApi::class)
//private fun initPlayer(context: Context): ExoPlayer {
//    return ExoPlayer.Builder(context).apply {
//        setSeekBackIncrementMs(10000)
//        setSeekForwardIncrementMs(10000)
//    }.build().apply {
//        setMediaItem(MediaItem.fromUri(""))
//        setAudioAttributes(AudioAttributes.DEFAULT, true)
//        prepare()
//        playWhenReady = false
//    }
//}