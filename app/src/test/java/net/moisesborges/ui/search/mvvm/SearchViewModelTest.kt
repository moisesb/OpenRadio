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

import com.nhaarman.mockitokotlin2.mock
import net.moisesborges.base.BaseViewModeTest
import net.moisesborges.base.testRxSchedulers
import net.moisesborges.features.search.SearchEngine
import org.amshove.kluent.`should be`
import org.junit.Ignore
import org.junit.Test

class SearchViewModelTest : BaseViewModeTest() {

    val searchEngine: SearchEngine = mock()

    lateinit var testSubject: SearchViewModel

    override fun setup() {
        testSubject = SearchViewModel(searchEngine, testRxSchedulers)
    }

    @Ignore("Fix it")
    @Test
    fun `when search nor started, then result must be empty`() {
        testSubject.result.extractValue() `should be` emptyList()
    }
}