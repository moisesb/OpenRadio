package net.moisesborges.bindingadapter

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("android:setVisibility")
fun setVisibilityBinding(view: View, isVisible: Boolean) {
    view.visibility = if (isVisible) View.VISIBLE else View.GONE
}