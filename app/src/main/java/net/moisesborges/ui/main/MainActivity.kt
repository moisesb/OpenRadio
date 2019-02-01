package net.moisesborges.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.google.android.material.bottomnavigation.BottomNavigationView
import net.moisesborges.R
import net.moisesborges.ui.base.LifecycleActivity
import net.moisesborges.ui.favorites.FavoritesRadioFragment
import net.moisesborges.ui.main.mvvm.RadioSelection
import net.moisesborges.ui.main.mvvm.MainViewModel
import net.moisesborges.ui.top.TopRadioFragment
import org.koin.android.ext.android.inject
import java.lang.IllegalStateException

class MainActivity : LifecycleActivity() {

    private val viewModel: MainViewModel by inject()

    private val toolbar: Toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private val bottomNavigation: BottomNavigationView by lazy { findViewById<BottomNavigationView>(R.id.bottom_navigation) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.radioSelection.observe(this, Observer {
            val fragment =  when(it) {
                RadioSelection.TOP -> TopRadioFragment()
                RadioSelection.FAVOURITES -> FavoritesRadioFragment()
                else -> throw IllegalStateException("Support for $it radio selection not implemented")
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.radio_selection_container, fragment)
                .commit()
        })

        toolbar.setOnMenuItemClickListener(this::onMenuItemSelected)
        bottomNavigation.setOnNavigationItemSelectedListener(this::onMenuItemSelected)
    }

    private fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId) {
            R.id.top_radios_menu_item -> viewModel.topSelected()
            R.id.favorites_radios_menu_item -> viewModel.favoritesSelected()
            else -> return false
        }
        return true
    }
}
