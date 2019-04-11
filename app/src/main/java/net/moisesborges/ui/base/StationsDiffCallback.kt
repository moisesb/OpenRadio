package net.moisesborges.ui.base

import androidx.recyclerview.widget.DiffUtil
import net.moisesborges.model.Station

class StationsDiffCallback(
    private val oldStations: List<Station>,
    private val newStations: List<Station>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldStations[oldItemPosition].id == newStations[newItemPosition].id
    }

    override fun getOldListSize(): Int {
        return oldStations.size
    }

    override fun getNewListSize(): Int {
        return newStations.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldStations[oldItemPosition] == newStations[newItemPosition]
    }
}