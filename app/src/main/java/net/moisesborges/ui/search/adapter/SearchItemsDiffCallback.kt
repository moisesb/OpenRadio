package net.moisesborges.ui.search.adapter

import androidx.recyclerview.widget.DiffUtil
import net.moisesborges.ui.search.mvvm.SearchItem

class SearchItemsDiffCallback(
    private val newSearchItems: List<SearchItem>,
    private val oldSearchItems: List<SearchItem>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldSearchItems[oldItemPosition] == newSearchItems[newItemPosition]
    }

    override fun getOldListSize(): Int {
        return oldSearchItems.size
    }

    override fun getNewListSize(): Int {
        return newSearchItems.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldSearchItems[oldItemPosition] == newSearchItems[newItemPosition]
    }
}