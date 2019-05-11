package net.moisesborges.ui.base

import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView

class CancellableListUpdateCallback(
    private val adapter: RecyclerView.Adapter<*>
) : ListUpdateCallback {

    private var isCancelled = false

    fun cancel() {
        isCancelled = true
    }

    @Synchronized
    override fun onChanged(position: Int, count: Int, payload: Any?) {
        if (isCancelled) return
        adapter.notifyItemRangeChanged(position, count, payload)
    }

    @Synchronized
    override fun onMoved(fromPosition: Int, toPosition: Int) {
        if (isCancelled) return
        adapter.notifyItemMoved(fromPosition, toPosition)
    }

    @Synchronized
    override fun onInserted(position: Int, count: Int) {
        if (isCancelled) return
        adapter.notifyItemRangeInserted(position, count)
    }

    @Synchronized
    override fun onRemoved(position: Int, count: Int) {
        if (isCancelled) return
        adapter.notifyItemRangeRemoved(position, count)
    }
}