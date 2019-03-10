package net.moisesborges.ui.top.mvvm

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