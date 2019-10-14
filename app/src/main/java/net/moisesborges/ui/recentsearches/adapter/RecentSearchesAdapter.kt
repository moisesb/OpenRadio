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

package net.moisesborges.ui.recentsearches.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import net.moisesborges.R
import net.moisesborges.databinding.HeaderItemBinding
import net.moisesborges.databinding.RecentViewedStationItemBinding
import net.moisesborges.ui.base.CancellableListUpdateCallback
import net.moisesborges.ui.base.LifecycleViewHolder
import net.moisesborges.ui.recentsearches.mvvm.RecentSearchItem
import net.moisesborges.utils.ContentDiffCallback

private const val HEADER_VIEW_TYPE = 0
private const val RECENT_VIEWED_STATION_VIEW_TYPE = 1

class RecentSearchesAdapter(
    private val viewModelFactory: RecentViewedStationItemViewModelFactory
) : RecyclerView.Adapter<RecentSearchesContentViewHolder>() {

    var content: List<RecentSearchItem> = mutableListOf()
        set(newValue) {
            val oldValue = content
            cancellableListUpdateCallback.cancel()
            field = newValue
            val diffResult = DiffUtil.calculateDiff(ContentDiffCallback(oldValue, newValue))
            cancellableListUpdateCallback = CancellableListUpdateCallback(this)
            diffResult.dispatchUpdatesTo(cancellableListUpdateCallback)
        }
    private var cancellableListUpdateCallback = CancellableListUpdateCallback(this)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSearchesContentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            HEADER_VIEW_TYPE -> {
                val binding: HeaderItemBinding =
                    DataBindingUtil.inflate(layoutInflater, R.layout.header_item, parent, false)
                RecentSearchesContentViewHolder.Header(binding)
            }

            RECENT_VIEWED_STATION_VIEW_TYPE -> {
                val binding: RecentViewedStationItemBinding = DataBindingUtil.inflate(
                    layoutInflater,
                    R.layout.recent_viewed_station_item,
                    parent,
                    false
                )
                RecentSearchesContentViewHolder.RecentViewedStation(binding, viewModelFactory)
            }

            else -> throw UnsupportedOperationException("view holder for view type $viewType not implemented")
        }
    }

    override fun getItemCount(): Int {
        return content.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (content[position]) {
            is RecentSearchItem.Header -> HEADER_VIEW_TYPE
            is RecentSearchItem.RecentViewedStation -> RECENT_VIEWED_STATION_VIEW_TYPE
        }
    }

    override fun onBindViewHolder(holder: RecentSearchesContentViewHolder, position: Int) {
        val contentItem = content[position]
        holder.bind(contentItem)
    }
}

sealed class RecentSearchesContentViewHolder(binding: ViewDataBinding) : LifecycleViewHolder(binding) {

    abstract fun bind(item: RecentSearchItem)

    class Header(
        val binding: HeaderItemBinding
    ) : RecentSearchesContentViewHolder(binding) {
        override fun bind(item: RecentSearchItem) {
            if (item !is RecentSearchItem.Header) {
                return
            }
            binding.viewModel = item
        }
    }

    class RecentViewedStation(
        val binding: RecentViewedStationItemBinding,
        private val viewModelFactory: RecentViewedStationItemViewModelFactory
    ) : RecentSearchesContentViewHolder(binding) {
        override fun bind(item: RecentSearchItem) {
            if (item !is RecentSearchItem.RecentViewedStation) {
                return
            }
            binding.viewModel = viewModelFactory.create(item.station)
        }
    }
}