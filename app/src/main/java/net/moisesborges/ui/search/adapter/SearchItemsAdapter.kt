package net.moisesborges.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import net.moisesborges.R
import net.moisesborges.databinding.ProgressIndicatorItemBinding
import net.moisesborges.databinding.TopStationItemBinding
import net.moisesborges.ui.base.CancellableListUpdateCallback
import net.moisesborges.ui.base.LifecycleViewHolder
import net.moisesborges.ui.search.mvvm.SearchItem
import net.moisesborges.ui.top.adapter.TopStationItemViewModelFactory

private const val STATION_VIEW_TYPE = 0
private const val PROGRESS_INDICATOR_VIEW_TYPE = 1

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

    abstract fun bind(searchItem: SearchItem)
}