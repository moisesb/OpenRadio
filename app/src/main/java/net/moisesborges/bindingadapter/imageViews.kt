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

package net.moisesborges.bindingadapter

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import net.moisesborges.R
import net.moisesborges.model.ImageUrl
import timber.log.Timber
import java.lang.Exception

private val callback = object : Callback {
    override fun onSuccess() {
        // Do nothing
    }

    override fun onError(e: Exception?) {
        Timber.e("Could not load the image. Error: $e, message: ${e?.localizedMessage}")
    }
}

@BindingAdapter("setImage")
fun setImageBinding(view: ImageView, imageUrl: ImageUrl?) {
    val url = imageUrl?.url ?: return
    if (url.isNotEmpty()) {
        Picasso.get()
            .load(url)
            .into(view, callback)
    }
}

@BindingAdapter("togglePlayPause")
fun togglePlayPauseBinding(view: ImageView, isPlaying: Boolean) {
    @DrawableRes val res = if (!isPlaying) R.drawable.ic_baseline_play_circle_filled_24px
    else R.drawable.ic_baseline_pause_circle_filled_24px
    val drawable = ContextCompat.getDrawable(view.context, res)
    view.setImageDrawable(drawable)
}

@BindingAdapter("toggleFavorite")
fun toggleFavoriteBinding(view: ImageView, isSaved: Boolean) {
    @DrawableRes val res = if (!isSaved) R.drawable.ic_baseline_favorite_border_24px
    else R.drawable.ic_baseline_favorite_24px
    val drawable = ContextCompat.getDrawable(view.context, res)
    view.setImageDrawable(drawable)
}