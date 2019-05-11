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