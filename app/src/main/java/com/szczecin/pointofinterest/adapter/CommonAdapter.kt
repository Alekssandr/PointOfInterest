package com.szczecin.pointofinterest.adapter

import android.text.method.LinkMovementMethod
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.szczecin.pointofinterest.R


@BindingAdapter("imgUrl")
fun ImageView.loadImage(url: String?) {
    url?.let {
        Glide.with(context)
            .load(url)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background)
            )
            .into(this)
    }
}

@BindingAdapter("imageList")
fun RecyclerView.bindItems(items: List<String>?) {

    items?.let {
        val adapter = adapter as ImageItemsAdapter
        adapter.update(items)
    }
}

@BindingAdapter("wikiLink")
fun TextView.loadUrl(wikiLink: String?) {
    wikiLink?.let {
        text = HtmlCompat.fromHtml(
            String.format("<a href=\"%s\">Wikipedia</a> ", wikiLink),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        movementMethod = LinkMovementMethod.getInstance()
    }

}