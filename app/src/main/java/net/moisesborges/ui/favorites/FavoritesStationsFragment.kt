package net.moisesborges.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import net.moisesborges.R
import net.moisesborges.databinding.FragmentFavoritesStationsBinding
import net.moisesborges.ui.base.LifecycleFragment
import net.moisesborges.ui.favorites.adapter.FavoriteStationItemViewModelFactory
import net.moisesborges.ui.favorites.adapter.FavoriteStationsAdapter
import net.moisesborges.ui.favorites.mvvm.FavoritesViewModel
import org.koin.android.ext.android.inject

class FavoritesStationsFragment : LifecycleFragment() {

    private val viewModel: FavoritesViewModel by inject()
    private val itemViewModelFactory: FavoriteStationItemViewModelFactory by inject()
    private val adapter: FavoriteStationsAdapter = FavoriteStationsAdapter(itemViewModelFactory)

    private lateinit var binding: FragmentFavoritesStationsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites_stations, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        binding.favoriteStationsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.favoriteStationsRecyclerView.adapter = adapter

        viewModel.favorites.observe(this, Observer {
            adapter.setStations(it)
        })
    }
}