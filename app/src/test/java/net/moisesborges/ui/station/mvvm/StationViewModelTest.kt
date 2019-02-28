package net.moisesborges.ui.station.mvvm

import net.moisesborges.ui.navigation.Navigator
import org.junit.Test
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import net.moisesborges.base.BaseViewModeTest
import net.moisesborges.base.createTestStation

class StationViewModelTest : BaseViewModeTest() {

    val station = createTestStation()

    val navigator: Navigator = mock()

    lateinit var testSubject: StationViewModel

    override fun setup() {
        testSubject = StationViewModel(station, navigator)
    }

    @Test fun `when closeSelected, then it should navigateBack`() {
        testSubject.closeSelected()

        verify(navigator).navigateBack()
    }
}