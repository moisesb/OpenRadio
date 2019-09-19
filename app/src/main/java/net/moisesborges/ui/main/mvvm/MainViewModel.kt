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

package net.moisesborges.ui.main.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import net.moisesborges.extensions.plusAssign
import net.moisesborges.ui.navigation.Navigator
import net.moisesborges.ui.navigation.TabNavigator
import net.moisesborges.ui.navigation.TabSection

class MainViewModel(
    private val navigator: Navigator,
    private val tabNavigator: TabNavigator
) : ViewModel() {

    private val mutableTabSection = MutableLiveData<TabSection>()

    private val disposables = CompositeDisposable()

    val tabSection: LiveData<TabSection> = Transformations.map(mutableTabSection) { it }

    init {
        disposables += tabNavigator.currentTabSection()
            .subscribe { this.mutableTabSection.postValue(it) }
    }

    fun homeSelected() {
        tabNavigator.navigateToHome()
    }

    fun myStationsSelected() {
        tabNavigator.navigateToMyStations()
    }

    fun recentSearchesSelected() {
        tabNavigator.navigateToRecentSearches()
    }

    fun searchSelected() {
        navigator.navigateToSearch()
    }

    fun clear() {
        disposables.clear()
    }
}
