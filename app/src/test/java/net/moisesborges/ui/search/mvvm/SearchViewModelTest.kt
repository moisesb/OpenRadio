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

package net.moisesborges.ui.search.mvvm

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import net.moisesborges.base.BaseViewModeTest
import net.moisesborges.base.givenNextStation
import net.moisesborges.base.testRxSchedulers
import net.moisesborges.features.search.SearchEngine
import net.moisesborges.features.search.SearchResult
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should equal`
import org.junit.Assert.fail
import org.junit.Test

class SearchViewModelTest : BaseViewModeTest() {

    val searchEngine: SearchEngine = mock()

    val stringResolver: SearchStringResolver = mock()

    lateinit var testSubject: SearchViewModel

    override fun setup() {
        testSubject = SearchViewModel(searchEngine, testRxSchedulers, stringResolver)
    }

    @Test fun `when search nor started, then result must be empty`() {
        testSubject.result.extractValue() `should equal` emptyList()
    }

    @Test fun `when search started but not finished, then result must be progress indicator`() {
        val searchSubject: Subject<SearchResult> = BehaviorSubject.create()
        whenever(searchEngine.searchResult()).thenReturn(searchSubject)
        testSubject.start()

        testSubject.search("Some Stations")

        testSubject.result.extractValue() `should equal` listOf(SearchItem.ProgressIndicator)
    }

    @Test fun `when search started and finished, then result must be actual items`() {
        val firstStation = givenNextStation("url1")
        val secondStation = givenNextStation("url2")
        val stations = listOf(firstStation, secondStation)
        val mockResult = SearchResult(false, stations)
        val searchSubject: Subject<SearchResult> = BehaviorSubject.create()
        whenever(searchEngine.searchResult()).thenReturn(searchSubject)

        testSubject.start()

        testSubject.search("Some Stations")

        searchSubject.onNext(mockResult)

        val result = testSubject.result.extractValue()
        result.size `should be equal to` stations.size
        result.zip(stations).forEach { (searchItem, expected) ->
            if (searchItem !is SearchItem.Station) {
                fail("SearchItem.Station is expected")
                return
            }
            searchItem.station `should be` expected
        }
    }

    @Test fun `when search started and finished but result is empty, then result must be empty message`() {
        val mockResult = SearchResult(false, emptyList())
        val searchSubject: Subject<SearchResult> = BehaviorSubject.create()
        whenever(searchEngine.searchResult()).thenReturn(searchSubject)
        val expectedMessage = "some message"
        whenever(stringResolver.emptyResultsForQuery(any())).doReturn(expectedMessage)

        testSubject.start()

        testSubject.search("Some Stations")

        searchSubject.onNext(mockResult)

        val result = testSubject.result.extractValue()
        result.size `should be equal to` 1
        val item = result.first()
        if (item !is SearchItem.EmptyResultsMessage) {
            fail("SearchItem.EmptyResultsMessage is expected")
            return
        }

        item.message `should be equal to` expectedMessage
    }
}