package net.moisesborges.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import net.moisesborges.R
import net.moisesborges.databinding.ActivityMainBinding
import net.moisesborges.ui.audioplayer.AudioPlayerViewModel
import net.moisesborges.ui.base.LifecycleActivity
import net.moisesborges.ui.favorites.FavoritesStationsFragment
import net.moisesborges.ui.main.mvvm.PageSelection
import net.moisesborges.ui.main.mvvm.MainViewModel
import net.moisesborges.ui.recentsearches.RecentSearchesFragment
import net.moisesborges.ui.home.HomeFragment
import org.koin.android.ext.android.get
import java.lang.IllegalStateException

class MainActivity : LifecycleActivity() {

    private val mainViewModel: MainViewModel = get()
    private val audioPlayerViewModel: AudioPlayerViewModel = get()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.audioPlayerViewModel = audioPlayerViewModel

        mainViewModel.pageSelection.observe(this, Observer {
            val fragment = when (it) {
                PageSelection.HOME -> HomeFragment()
                PageSelection.MY_STATIONS -> FavoritesStationsFragment()
                PageSelection.RECENT_SEARCHES -> RecentSearchesFragment()
                else -> throw IllegalStateException("Support for $it radio selection not implemented")
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.radio_selection_container, fragment)
                .commit()
        })

        binding.toolbar.setOnMenuItemClickListener(this::onMenuItemSelected)
        binding.bottomNavigation.setOnNavigationItemSelectedListener(this::onMenuItemSelected)

        setSupportActionBar(binding.toolbar)
    }

    override fun onDestroy() {
        audioPlayerViewModel.clear()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_top_menu, menu)
        return true
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
