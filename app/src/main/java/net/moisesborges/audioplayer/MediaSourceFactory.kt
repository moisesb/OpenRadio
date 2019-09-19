/*
 * MIT License
 *
 * Copyright (c) 2019 Moisés Borges dos Anjos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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