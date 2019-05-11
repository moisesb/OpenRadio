package net.moisesborges.ui.base

import androidx.recyclerview.widget.RecyclerView

abstract class LifecycleRecyclerViewAdapter<ViewHolder : LifecycleViewHolder> : RecyclerView.Adapter<ViewHolder>() {

    override fun onViewAttachedToWindow(viewHolder: ViewHolder) {
        super.onViewAttachedToWindow(viewHolder)
        viewHolder.attachToWindow()
    }

    override fun onViewDetachedFromWindow(viewHolder: ViewHolder) {
        super.onViewDetachedFromWindow(viewHolder)
        viewHolder.detachFromWindow()
    }
}