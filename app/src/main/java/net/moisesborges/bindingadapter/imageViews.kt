package net.moisesborges.bindingadapter

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import net.moisesborges.R
import net.moisesborges.model.Image

@BindingAdapter("setImage")
fun setImageBinding(view: ImageView, image: Image?) {
    Picasso.get()
        .load(image?.url)
        .into(view)
}

@BindingAdapter("togglePlayPause")
fun togglePlayPauseBinding(view: ImageView, isPlaying: Boolean) {
    @DrawableRes val res = if (!isPlaying) R.drawable.ic_baseline_play_circle_filled_24px
    else R.drawable.ic_baseline_pause_circle_filled_24px
    val drawable = ContextCompat.getDrawable(view.context, res)
    view.setImageDrawable(drawable)
}