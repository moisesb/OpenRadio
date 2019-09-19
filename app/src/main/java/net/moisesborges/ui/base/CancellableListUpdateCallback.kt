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