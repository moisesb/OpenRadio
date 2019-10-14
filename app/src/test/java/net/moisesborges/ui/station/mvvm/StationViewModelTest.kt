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

package net.moisesborges.ui.station.mvvm

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.never
import io.reactivex.subjects.BehaviorSubject
import net.moisesborges.ui.navigation.Navigator
import org.junit.Test
import net.moisesborges.api.OpenRadioApi
import net.moisesborges.audioplayer.AudioPlayer
import net.moisesborges.audioplayer.PlaybackState
import net.moisesborges.base.BaseViewModeTest
import net.moisesborges.base.givenNextStation
import net.moisesborges.base.testRxSchedulers

class StationViewModelTest : BaseViewModeTest() {

    val playbackState = BehaviorSubject.create<PlaybackState>()
    val audioPlayer: AudioPlayer = mock {
        on { playbackState() } doReturn playbackState
    }
    val openRadioApi: OpenRadioApi = mock()
    val navigator: Navigator = mock()

    lateinit var testSubject: StationViewModel

    override fun setup() {
        testSubject = StationViewModel(audioPlayer, openRadioApi, navigator, testRxSchedulers)
    }

    @Test fun `when closeSelected, then it should navigateBack`() {
        testSubject.closeSelected()

        verify(navigator).navigateBack()
    }

    @Test fun `given prepareNextStation, when playPause, then it should load and play next station`() {
        val expectedStationId = 1
        val expectedStreamUrl = "streamUrl"
        val station = givenNextStation(expectedStreamUrl)
        whenever(station.id).doReturn(expectedStationId)
        testSubject.prepareStation(station)

        testSubject.playPause()

        verify(audioPlayer).load(station)
    }

    @Test fun `given prepareStation and clear, when playPause, then it should not load and play next station`() {
        val expectedStreamUrl = "streamUrl"
        val station = givenNextStation(expectedStreamUrl)
        testSubject.prepareStation(station)
        testSubject.clear()

        testSubject.playPause()

        verify(audioPlayer, never()).load(station)
    }

    @Test fun `given prepareStation was not called and audioPlayer is playing, when playPause, then it should not play or pause`() {
        whenever(audioPlayer.isPlaying()).thenReturn(true)

        testSubject.playPause()

        verify(audioPlayer, never()).pause()
        verify(audioPlayer, never()).play()
    }

    @Test fun `given prepareStation was not called and audioPlayer is not playing, when playPause, then it should play`() {
        whenever(audioPlayer.isPlaying()).thenReturn(false)

        testSubject.playPause()

        verify(audioPlayer, never()).pause()
        verify(audioPlayer, never()).play()
    }
}