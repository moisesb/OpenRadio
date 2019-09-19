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

package net.moisesborges.ui.top.mvvm

import net.moisesborges.ui.home.mvvm.PaginationManager
import org.junit.Test

class PaginationManagerTest {

    val testSubject = PaginationManager()

    @Test fun `given no interaction with testSubject, then loadPage zero`() {
        val expectedPage = 0

        testSubject.loadPage().test()
            .assertValue(expectedPage)
    }

    @Test fun `given item displayed is not last, when onItemDisplayed, then not load new page`() {
        testSubject.onItemDisplayed(5, 20)

        testSubject.loadPage().test()
            .assertValue(0)
    }

    @Test fun `given last item is displayed, when onItemDisplayed, then load new page`() {
        testSubject.onItemDisplayed(19, 20)

        testSubject.loadPage().test()
            .assertValues(1)
    }

    @Test fun `given last item displayed twice, when onItemDisplayed, then load new page once`() {
        testSubject.onItemDisplayed(19, 20)
        testSubject.onItemDisplayed(19, 20)

        testSubject.loadPage().test()
            .assertValue(1)
    }
}