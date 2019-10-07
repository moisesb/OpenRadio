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

package net.moisesborges.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import net.moisesborges.R
import net.moisesborges.databinding.EmptyResultsItemBinding
import net.moisesborges.databinding.ProgressIndicatorItemBinding
import net.moisesborges.databinding.SearchErrorItemBinding
import net.moisesborges.databinding.TopStationItemBinding
import net.moisesborges.ui.base.CancellableListUpdateCallback
import net.moisesborges.ui.base.LifecycleViewHolder
import net.moisesborges.ui.search.mvvm.SearchItem
import net.moisesborges.ui.home.adapter.TopStationItemViewModelFactory

private const val STATION_VIEW_TYPE = 0
private const val PROGRESS_INDICATOR_VIEW_TYPE = 1
private const val EMPTY_RESULTS_VIEW_TYPE = 2
private const val ERROR_VIEW_TYPE = 3

class SearchItemsAdapter(
    private val viewModelFactory: TopStationItemViewModelFactory
) : RecyclerView.Adapter<SearchItemViewHolder>() {

    private var searchItems: List<SearchItem> = mutableListOf()
    private var oldSearchItems: List<SearchItem> = mutableListOf()
    private var cancellableListUpdateCallback = CancellableListUpdateCallback(this)

    fun setSearchItems(searchItems: List<SearchItem>) {
        cancellableListUpdateCallback.cancel()
        this.oldSearchItems = this.searchItems
        this.searchItems = searchItems
        val diffResult = DiffUtil.calculateDiff(SearchItemsDiffCallback(this.searchItems, this.oldSearchItems))
        cancellableListUpdateCallback = CancellableListUpdateCallback(this)
        diffResult.dispatchUpdatesTo(cancellableListUpdateCallback)
    }

    override fun getItemViewType(position: Int): Int {
        return when (searchItems[position]) {
            is SearchItem.Station -> STATION_VIEW_TYPE
            is SearchItem.ProgressIndicator -> PROGRESS_INDICATOR_VIEW_TYPE
            is SearchItem.EmptyResultsMessage -> EMPTY_RESULTS_VIEW_TYPE
            is SearchItem.ErrorMessage -> ERROR_VIEW_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            STATION_VIEW_TYPE -> {
                val binding: TopStationItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.top_station_item, parent, false)
                SearchItemViewHolder.Station(binding, viewModelFactory)
            }
            PROGRESS_INDICATOR_VIEW_TYPE -> {
                val binding: ProgressIndicatorItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.progress_indicator_item, parent, false)
                SearchItemViewHolder.ProgressIndicator(binding)
            }
            EMPTY_RESULTS_VIEW_TYPE -> {
                val binding: EmptyResultsItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.empty_results_item, parent, false)
                SearchItemViewHolder.EmptyResults(binding)
            }
            ERROR_VIEW_TYPE -> {
                val binding: SearchErrorItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.search_error_item, parent, false)
                SearchItemViewHolder.ErrorMessage(binding)
            }
            else -> {
                throw IllegalStateException("view type $viewType not supported")
            }
        }
    }

    override fun getItemCount(): Int {
        return searchItems.size
    }

    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
        val searchItem = searchItems[position]
        when (holder) {
            is SearchItemViewHolder.Station -> holder.bind(searchItem)
            is SearchItemViewHolder.EmptyResults -> holder.bind(searchItem)
            is SearchItemViewHolder.ErrorMessage -> holder.bind(searchItem)
        }
    }
}

sealed class SearchItemViewHolder(binding: ViewDataBinding) : LifecycleViewHolder(binding) {

    class Station(
        val binding: TopStationItemBinding,
        private val viewModelFactory: TopStationItemViewModelFactory
    ) : SearchItemViewHolder(binding) {

        override fun bind(searchItem: SearchItem) {
            if (searchItem !is SearchItem.Station) {
                return
            }
            binding.viewModel = viewModelFactory.create(searchItem.station)
        }
    }

    class ProgressIndicator(val binding: ProgressIndicatorItemBinding) : SearchItemViewHolder(binding) {

        override fun bind(searchItem: SearchItem) {
            // Blank
        }
    }

    class EmptyResults(
        val binding: EmptyResultsItemBinding
    ) : SearchItemViewHolder(binding) {

        override fun bind(searchItem: SearchItem) {
            if (searchItem !is SearchItem.EmptyResultsMessage) {
                return
            }
            binding.message.text = searchItem.message
        }
    }

    class ErrorMessage(
        val binding: SearchErrorItemBinding
    ) : SearchItemViewHolder(binding) {

        override fun bind(searchItem: SearchItem) {
            if (searchItem !is SearchItem.ErrorMessage) {
                return
            }

            binding.message.text = searchItem.message
        }
    }

    abstract fun bind(searchItem: SearchItem)
}