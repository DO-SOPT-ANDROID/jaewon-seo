package com.sopt.common.imageview

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.CircleCropTransformation

@BindingAdapter("imageUrl")
fun ImageView.setImageCircleUrl(imageUrl: String?) {
    imageUrl?.let {
        load(it) {
            transformations(CircleCropTransformation())
        }
    }
}

@BindingAdapter("imageResource")
fun ImageView.setImageCircleResource(imageResource: Int) {
    if (imageResource != 0) {
        load(imageResource) {
            transformations(CircleCropTransformation())
        }
    }
}
