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