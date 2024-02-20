package com.communication.pingyi.adapter

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.communication.pingyi.R

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(imageUrl)
//            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }
}

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: Int) {
    if (imageUrl != 0) {
        Glide.with(view.context)
            .load(imageUrl)
//            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }
}

@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("isComplete")
fun bindIsComplete(view: TextView, status: Int) {
    if (status == 0) {
        view.text = view.resources.getString(R.string.uncomplete)
        view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.red))
    } else {
        view.text = view.resources.getString(R.string.complete)
        view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.green))
    }
}

@BindingAdapter("highLight", "content")
fun bindHighLight(view: TextView, highLight: String, content: String) {
    val ss = SpannableString(content)
    if (highLight.isNotBlank()) {
        var index = -1
        do {
            index++
            index = content.indexOf(highLight, index, true)
            if (index >= 0) {
                ss.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(view.context, R.color.blue)),
                    index,
                    index + highLight.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        } while (index != -1)
    }
    view.text = ss
}