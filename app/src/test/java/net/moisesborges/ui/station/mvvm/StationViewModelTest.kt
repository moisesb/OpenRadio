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
import net.moisesborges.model.Station
import net.moisesborges.model.StreamUrl

class StationViewModelTest : BaseViewModeTest() {

    val playbackState = BehaviorSubject.create<PlaybackState>()
    val audioPlayer: AudioPlayer = mock {
        on { playbackState() } doReturn playbackState
    }
    val openRadioApi: OpenRadioApi = mock()
    val navigator: Navigator = mock()

    lateinit var testSubject: StationViewModel

    override fun setup() {
        testSubject = StationViewModel(audioPlayer, openRadioApi, navigator)
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

    private fun givenNextStation(expectedStreamUrl: String): Station {
        return mock {
            on { streamUrl } doReturn StreamUrl(expectedStreamUrl)
        }
    }
}