package com.elanyudho.core.util.extension

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.glide(view: View, image: Any) {
    try {
        Glide.with(view)
            .load(image)
            .into(this)
    } catch (ignored: Throwable) {
    }
}

fun ImageView.glide(context: Context, image: Any) {
    try {
        Glide.with(context)
            .load(image)
            .into(this)
    } catch (ignored: Throwable) {
    }
}