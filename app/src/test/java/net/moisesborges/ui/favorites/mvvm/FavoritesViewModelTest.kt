package net.moisesborges.ui.favorites.mvvm

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.subjects.BehaviorSubject
import net.moisesborges.base.BaseViewModeTest
import net.moisesborges.base.testRxSchedulers
import net.moisesborges.features.favorite.FavoriteStationManager
import net.moisesborges.model.Station
import net.moisesborges.ui.navigation.TabNavigator
import org.amshove.kluent.`should be`
import org.junit.Test

class FavoritesViewModelTest : BaseViewModeTest() {
    val favoriteSubject = BehaviorSubject.create<List<Station>>()

    val favoriteStationManager: FavoriteStationManager = mock {
        on { favoriteStations() } doReturn favoriteSubject
    }

    val tabNavigator: TabNavigator = mock()

    lateinit var testSubject: FavoritesViewModel

    override fun setup() {
        testSubject = FavoritesViewModel(favoriteStationManager, testRxSchedulers, tabNavigator)
    }

    @Test fun `given did not finish load, then it should not be empty`() {
        val empty = testSubject.isEmpty.extractValue()

        empty `should be` false
    }

    @Test fun `given load empty station list, then it should be empty`() {
        favoriteSubject.onNext(emptyList())

        val empty = testSubject.isEmpty.extractValue()

        empty `should be` true
    }

    @Test fun `given load stations, then it should not be empty`() {
        favoriteSubject.onNext(listOf(mock()))

        val empty = testSubject.isEmpty.extractValue()

        empty `should be` false
    }

    @Test fun `given load stations, then it should return stations`() {
        val expectedStations = listOf<Station>(mock())
        favoriteSubject.onNext(expectedStations)

        val stations = testSubject.favorites.extractValue()

        stations `should be` expectedStations
    }

    @Test fun `given explore button is selected, then it should navigate to home`() {
        testSubject.exploreSelected()

        verify(tabNavigator).navigateToHome()
    }
}