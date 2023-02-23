package com.kerimbr.kotnews.core.utils.extensions

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kerimbr.kotnews.R


fun ImageView.getImageFromUrl(url: String) {
    val uri: Uri = Uri.parse(url)

    val option: RequestOptions = RequestOptions()
        .placeholder(R.drawable.logo_shimmer)
        .error(R.drawable.ic_baseline_error_24)

    Glide.with(this.context)
        .setDefaultRequestOptions(option)
        .load(uri)
        .fitCenter()
        .into(this)
}