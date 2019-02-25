package net.moisesborges.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import net.moisesborges.R
import net.moisesborges.databinding.ActivityMainBinding
import net.moisesborges.ui.audioplayer.AudioPlayerViewModel
import net.moisesborges.ui.base.LifecycleActivity
import net.moisesborges.ui.favorites.FavoritesRadioFragment
import net.moisesborges.ui.main.mvvm.RadioSelection
import net.moisesborges.ui.main.mvvm.MainViewModel
import net.moisesborges.ui.top.TopStationsFragment
import org.koin.android.ext.android.get
import java.lang.IllegalStateException

class MainActivity : LifecycleActivity() {

    private val mainViewModel: MainViewModel = get()
    private val audioPlayerViewModel: AudioPlayerViewModel = get()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mainViewModel.radioSelection.observe(this, Observer {
            val fragment = when (it) {
                RadioSelection.TOP -> TopStationsFragment()
                RadioSelection.FAVOURITES -> FavoritesRadioFragment()
                else -> throw IllegalStateException("Support for $it radio selection not implemented")
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.radio_selection_container, fragment)
                .commit()
        })

        binding.toolbar.setOnMenuItemClickListener(this::onMenuItemSelected)
        binding.bottomNavigation.setOnNavigationItemSelectedListener(this::onMenuItemSelected)
    }

    private fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.top_radios_menu_item -> mainViewModel.topSelected()
            R.id.favorites_radios_menu_item -> mainViewModel.favoritesSelected()
            else -> return false
        }
        return true
    }
}
