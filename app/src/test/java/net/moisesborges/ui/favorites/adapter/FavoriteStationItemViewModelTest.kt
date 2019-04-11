package net.moisesborges.ui.favorites.adapter

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import net.moisesborges.base.BaseViewModeTest
import net.moisesborges.model.Station
import net.moisesborges.ui.navigation.Navigator
import org.junit.Test

class FavoriteStationItemViewModelTest : BaseViewModeTest() {

    val station: Station = mock()
    val navigator: Navigator = mock()

    lateinit var testSubject: FavoriteStationItemViewModel

    override fun setup() {
        testSubject = FavoriteStationItemViewModel(station, navigator)
    }

    @Test fun `when itemSelected, then it should navigate to station`() {
        testSubject.itemSelected()

        verify(navigator).navigateToAudioPlayer(station)
    }
}