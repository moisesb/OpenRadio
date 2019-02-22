package net.moisesborges.ui.audio.mvvm

import net.moisesborges.ui.audio.player.AudioPlayer
import net.moisesborges.ui.navigation.Navigator
import org.junit.Test
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.subjects.BehaviorSubject
import net.moisesborges.base.createTestStation
import net.moisesborges.ui.audio.player.PlaybackState
import org.junit.Before
import org.junit.Rule

class AudioPlayerViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    val station = createTestStation()

    val navigator: Navigator = mock()

    val playbackState = BehaviorSubject.create<PlaybackState>()

    val audioPlayer: AudioPlayer = mock {
        on { playbackState() } doReturn playbackState
    }

    lateinit var testSubject: AudioPlayerViewModel

    @Before fun setup() {
        testSubject = AudioPlayerViewModel(station, audioPlayer, navigator)
    }

    @Test fun `when closeSelected, then it should navigateBack`() {
        testSubject.closeSelected()

        verify(navigator).navigateBack()
    }

    @Test fun `given audioPlayer is playing, when playPause, then it should pause`() {
        whenever(audioPlayer.isPlaying()).thenReturn(true)

        testSubject.playPause()

        verify(audioPlayer).pause()
    }

    @Test fun `given audioPlayer is not playing, when playPause, then it should play`() {
        whenever(audioPlayer.isPlaying()).thenReturn(false)

        testSubject.playPause()

        verify(audioPlayer).play()
    }
}