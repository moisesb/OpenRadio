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