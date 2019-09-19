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

package net.moisesborges.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.*
import net.moisesborges.R
import net.moisesborges.databinding.FragmentHomeBinding
import net.moisesborges.ui.home.mvvm.HomeViewModel
import net.moisesborges.ui.base.LifecycleFragment
import net.moisesborges.ui.home.mvvm.PaginationDetector
import net.moisesborges.ui.home.adapter.TopStationItemViewModelFactory
import net.moisesborges.ui.home.adapter.TopStationsAdapter
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject

class HomeFragment : LifecycleFragment() {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by inject()
    private val topStationItemViewModelFactory: TopStationItemViewModelFactory = get()
    private val paginationDetector: PaginationDetector = get()

    private val adapter = TopStationsAdapter(
        topStationItemViewModelFactory,
        paginationDetector
    )
    private val stationsRecyclerView: RecyclerView by lazy { stations_recycler_view }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stationsRecyclerView.layoutManager = LinearLayoutManager(view.context)
        stationsRecyclerView.setHasFixedSize(true)
        stationsRecyclerView.adapter = adapter

        binding.viewModel = viewModel

        viewModel.stations.observe(this, Observer { stations ->
            adapter.setStations(stations)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()

        viewModel.clear()
    }
}