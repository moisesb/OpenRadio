package net.moisesborges.ui.top.adapter

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import net.moisesborges.base.BaseViewModeTest
import net.moisesborges.base.createTestStation
import net.moisesborges.base.testRxSchedulers
import net.moisesborges.features.favorite.FavoriteStationManager
import net.moisesborges.ui.navigation.Navigator
import org.amshove.kluent.`should be equal to`
import org.junit.Test

class TopStationItemViewModelTest : BaseViewModeTest() {

    val station = createTestStation()

    val favoriteState = BehaviorSubject.create<Boolean>()

    val favoriteStationManager: FavoriteStationManager = mock {
        on { favoriteState(any()) } doReturn Single.just(false)
    }

    val navigator: Navigator = mock()

    lateinit var testSubject: TopStationItemViewModel

    override fun setup() {
        testSubject = TopStationItemViewModel(station, navigator, favoriteStationManager, testRxSchedulers)
    }

    @Test fun `When itemSelected, the should navigate to audio player for that station`() {
        whenever(favoriteStationManager.toggleState(any())).doReturn(Single.just(true))

        testSubject.itemSelected()

        verify(navigator).navigateToAudioPlayer(station)
    }

    @Test fun `When favoriteSelected, then it should call toggleState`() {
        whenever(favoriteStationManager.toggleState(any())).doReturn(Single.just(false))

        testSubject.favoriteSelected()

        verify(favoriteStationManager).toggleState(station)
    }

    @Test fun `given station was not saved, when favoriteSelected, then isFavorite should be true`() {
        whenever(favoriteStationManager.toggleState(any())).doReturn(Single.just(true))

        testSubject.favoriteSelected()

        testSubject.isFavorite.extractValue() `should be equal to` true
    }

    @Test fun `given station was saved, when favoriteSelected, then isFavorite should be false`() {
        whenever(favoriteStationManager.toggleState(any())).doReturn(Single.just(false))

        testSubject.favoriteSelected()

        testSubject.isFavorite.extractValue() `should be equal to` false
    }
}