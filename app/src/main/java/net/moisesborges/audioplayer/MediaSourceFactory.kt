package net.moisesborges.audioplayer

import android.content.Context
import androidx.core.net.toUri
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util

private const val APPLICATION_NAME = "Open Radio"

class MediaSourceFactory(private val context: Context) {

    private var dataSourceFactory: DataSource.Factory =
        DefaultHttpDataSourceFactory(Util.getUserAgent(context, APPLICATION_NAME))

    private val hlsMediaSourceFactory = HlsMediaSource.Factory(dataSourceFactory)

    private val progressiveMediaSourceFactory = ProgressiveMediaSource.Factory(dataSourceFactory)

    fun buildMediaSource(streamUri: String): MediaSource {
        val uri = streamUri.toUri()
        return when (Util.inferContentType(streamUri)) {
            C.TYPE_HLS -> hlsMediaSourceFactory.createMediaSource(uri)
            else -> progressiveMediaSourceFactory.createMediaSource(uri)
        }
    }
}