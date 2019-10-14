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

package net.moisesborges.ui.recentsearches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import net.moisesborges.R
import net.moisesborges.databinding.FragmentRecentSearchesBinding
import net.moisesborges.ui.base.LifecycleFragment
import net.moisesborges.ui.recentsearches.adapter.RecentSearchesAdapter
import net.moisesborges.ui.recentsearches.adapter.RecentViewedStationItemViewModelFactory
import net.moisesborges.ui.recentsearches.mvvm.RecentSearchesViewModel
import org.koin.android.ext.android.inject

class RecentSearchesFragment : LifecycleFragment() {

    private val viewModel: RecentSearchesViewModel by inject()
    private val recentViewedStationItemViewModelFactory: RecentViewedStationItemViewModelFactory by inject()

    private lateinit var binding: FragmentRecentSearchesBinding
    private val adapter = RecentSearchesAdapter(recentViewedStationItemViewModelFactory)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recent_searches, container, false)
        binding.viewModel = viewModel
        binding.contentRecyclerView.adapter = adapter

        viewModel.content.observe(this, Observer { recentSearchItems ->
            adapter.content = recentSearchItems
        })
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.start()
    }
}