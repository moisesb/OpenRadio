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

package net.moisesborges.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import net.moisesborges.R
import net.moisesborges.databinding.FragmentMyStationsBinding
import net.moisesborges.ui.base.LifecycleFragment
import net.moisesborges.ui.favorites.adapter.FavoriteStationItemViewModelFactory
import net.moisesborges.ui.favorites.adapter.FavoriteStationsAdapter
import net.moisesborges.ui.favorites.mvvm.FavoritesViewModel
import org.koin.android.ext.android.inject

class MyStationsFragment : LifecycleFragment() {

    private val viewModel: FavoritesViewModel by inject()
    private val itemViewModelFactory: FavoriteStationItemViewModelFactory by inject()
    private val adapter: FavoriteStationsAdapter = FavoriteStationsAdapter(itemViewModelFactory)

    private lateinit var binding: FragmentMyStationsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_stations, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        with(activity as AppCompatActivity) {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.title = getString(R.string.my_stations_toolbar_title)
        }
        setHasOptionsMenu(true)

        val numOfColumns = context?.resources?.getInteger(R.integer.saved_stations_columns) ?: throw IllegalStateException("context must not be null")
        binding.favoriteStationsRecyclerView.layoutManager = GridLayoutManager(context, numOfColumns)
        binding.favoriteStationsRecyclerView.adapter = adapter

        viewModel.favorites.observe(this, Observer {
            adapter.setStations(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_top_menu, menu)
    }
}