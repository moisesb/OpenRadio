package net.moisesborges.ui.audioplayer

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.whenever
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.subjects.BehaviorSubject
import net.moisesborges.audioplayer.AudioPlayer
import net.moisesborges.audioplayer.PlaybackState
import net.moisesborges.base.BaseViewModeTest
import net.moisesborges.extensions.get
import net.moisesborges.model.Station
import net.moisesborges.model.Stream
import org.amshove.kluent.`should be equal to`
import org.junit.Test

class AudioPlayerViewModelTest : BaseViewModeTest() {

    val playbackState = BehaviorSubject.create<PlaybackState>()

    val audioPlayer: AudioPlayer = mock {
        on { playbackState() } doReturn playbackState
    }

    lateinit var testSubject: AudioPlayerViewModel

    override fun setup() {
        testSubject = AudioPlayerViewModel(audioPlayer)
    }

    @Test fun `given prepareNextStation, when playPause, then it should load and play next station`() {
        val expectedStreamUrl = "streamUrl"
        val nextStation = givenNextStation(expectedStreamUrl)
        testSubject.prepareNextStation(nextStation)

        testSubject.playPause()

        val inOrder = inOrder(audioPlayer)
        inOrder.verify(audioPlayer).load(expectedStreamUrl)
        inOrder.verify(audioPlayer).play()
    }

    @Test fun `given prepareNextStation and clear, when playPause, then it should not load and play next station`() {
        val expectedStreamUrl = "streamUrl"
        val nextStation = givenNextStation(expectedStreamUrl)
        testSubject.prepareNextStation(nextStation)
        testSubject.clear()

        testSubject.playPause()

        verify(audioPlayer, never()).load(expectedStreamUrl)
    }

    @Test fun `given prepareNextStation was not called and audioPlayer is playing, when playPause, then it should pause`() {
        whenever(audioPlayer.isPlaying()).thenReturn(true)

        testSubject.playPause()

        verify(audioPlayer).pause()
    }

    @Test fun `given prepareNextStation was not called and audioPlayer is not playing, when playPause, then it should play`() {
        whenever(audioPlayer.isPlaying()).thenReturn(false)

        testSubject.playPause()

        verify(audioPlayer).play()
    }

    @Test fun `given playPause is called, when isEmpty, then it should be false`() {
        testSubject.prepareNextStation(givenNextStation("anyUrl"))
        testSubject.playPause()

        val isEmpty = testSubject.isEmpty.extractValue()

        isEmpty `should be equal to` false
    }

    @Test fun `given playPause was not called, when isEmpty, then it should be true`() {
        val isEmpty = testSubject.isEmpty.extractValue()

        isEmpty `should be equal to` true
    }

    private fun givenNextStation(expectedStreamUrl: String): Station {
        return mock {
            on { stream } doReturn Stream(expectedStreamUrl, 128, "audio/mp3")
        }
    }
}