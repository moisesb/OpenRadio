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

package net.moisesborges.ui.main

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import net.moisesborges.R
import net.moisesborges.databinding.ActivityMainBinding
import net.moisesborges.ui.audioplayer.AudioPlayerViewModel
import net.moisesborges.ui.base.LifecycleActivity
import net.moisesborges.ui.favorites.MyStationsFragment
import net.moisesborges.ui.main.mvvm.MainViewModel
import net.moisesborges.ui.recentsearches.RecentSearchesFragment
import net.moisesborges.ui.home.HomeFragment
import net.moisesborges.ui.navigation.TabSection
import org.koin.android.ext.android.get
import java.lang.IllegalStateException

class MainActivity : LifecycleActivity() {

    private val mainViewModel: MainViewModel = get()
    private val audioPlayerViewModel: AudioPlayerViewModel = get()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            with(window) {
                requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            }
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.audioPlayerViewModel = audioPlayerViewModel

        mainViewModel.tabSection.observe(this, Observer {
            val fragment = when (it) {
                TabSection.HOME -> HomeFragment()
                TabSection.MY_STATIONS -> MyStationsFragment()
                TabSection.RECENT_SEARCHES -> RecentSearchesFragment()
                else -> throw IllegalStateException("Support for $it radio selection not implemented")
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.radio_selection_container, fragment)
                .commit()
        })

        binding.bottomNavigation.setOnNavigationItemSelectedListener(this::onMenuItemSelected)
    }

    override fun onDestroy() {
        mainViewModel.clear()
        audioPlayerViewModel.clear()
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search_menu_item -> mainViewModel.searchSelected()
            else -> return false
        }
        return true
    }

    private fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.home_stations_menu_item -> mainViewModel.homeSelected()
            R.id.my_stations_menu_item -> mainViewModel.myStationsSelected()
            R.id.search_menu_item -> mainViewModel.recentSearchesSelected()
            else -> return false
        }
        return true
    }
}
