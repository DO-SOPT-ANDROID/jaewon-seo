package com.sopt.common.imageview

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.CircleCropTransformation

@BindingAdapter("imageUrl")
fun ImageView.setImageCircleUrl(imageUrl: String?) {
    if (imageUrl != null) {
        load(imageUrl) {
            transformations(CircleCropTransformation())
        }
    }
}
