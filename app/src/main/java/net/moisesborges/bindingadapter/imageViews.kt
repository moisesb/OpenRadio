package net.moisesborges.bindingadapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import net.moisesborges.model.Image

@BindingAdapter("setImage")
fun setImageBinding(view: ImageView, image: Image?) {
    Picasso.get()
        .load(image?.url)
        .into(view)
}