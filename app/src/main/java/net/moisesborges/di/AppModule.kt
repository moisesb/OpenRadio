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

package net.moisesborges.di

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import net.moisesborges.audioplayer.AudioPlayer
import net.moisesborges.audioplayer.AudioPlayerService
import net.moisesborges.audioplayer.MediaSourceFactory
import net.moisesborges.ui.navigation.ActivityProvider
import net.moisesborges.ui.navigation.Navigator
import net.moisesborges.ui.navigation.TabNavigator
import net.moisesborges.utils.BitmapFactory
import net.moisesborges.utils.RxSchedulers
import org.koin.dsl.module.module

val appModule = { context: Context ->
    module {
        factory { context.resources }
        single { ActivityProvider() }
        single { Navigator(get()) }
        single { TabNavigator() }
        factory<ExoPlayer> { ExoPlayerFactory.newSimpleInstance(get()) }
        single { MediaSourceFactory(get()) }
        single { AudioPlayerService.Launcher(get()) }
        single { AudioPlayer(get(), get()) }
        single { AudioPlayerService.AudioPlayerServiceBinder() }
        single { RxSchedulers() }
        single { BitmapFactory() }
    }
}