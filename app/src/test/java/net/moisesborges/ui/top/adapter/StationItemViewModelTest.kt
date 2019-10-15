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
import net.moisesborges.ui.home.adapter.StationItemViewModel
import net.moisesborges.ui.navigation.Navigator
import org.amshove.kluent.`should be equal to`
import org.junit.Test

class StationItemViewModelTest : BaseViewModeTest() {

    val station = createTestStation()

    val favoriteState = BehaviorSubject.create<Boolean>()

    val favoriteStationManager: FavoriteStationManager = mock {
        on { favoriteState(any()) } doReturn Single.just(false)
    }

    val navigator: Navigator = mock()

    lateinit var testSubject: StationItemViewModel

    override fun setup() {
        testSubject = StationItemViewModel(
            station,
            navigator,
            favoriteStationManager,
            testRxSchedulers
        )
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