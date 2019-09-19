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

package net.moisesborges.ui.home.mvvm

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class PaginationManager : PaginationLoader,
    PaginationDetector {

    private var page = 0
    private var loading = false
    private val loadPageSubject = BehaviorSubject.create<Int>()

    init {
        loadPageSubject.onNext(page)
    }

    override fun onPageLoaded() {
        loading = false
    }

    override fun onLoadFailed() {
        loading = false
    }

    override fun loadPage(): Observable<Int> {
        return loadPageSubject
    }

    override fun onItemDisplayed(itemPosition: Int, totalItems: Int) {
        val lastPosition = itemPosition == totalItems - 1
        if (lastPosition && !loading && totalItems > 0) {
            loadNextPage(++page)
        }
    }

    private fun loadNextPage(pageNumber: Int) {
        loading = true
        loadPageSubject.onNext(pageNumber)
    }
}

interface PaginationLoader {

    fun onPageLoaded()

    fun onLoadFailed()

    fun loadPage(): Observable<Int>
}

interface PaginationDetector {

    fun onItemDisplayed(itemPosition: Int, totalItems: Int)
}