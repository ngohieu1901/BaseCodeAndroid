package com.hieunt.base.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

fun View.layoutInflate(): LayoutInflater = LayoutInflater.from(context)

fun View.tap(action: (view: View?) -> Unit) {
    setOnClickListener {
        it.isEnabled = false
        it.postDelayed({ it.isEnabled = true }, 1500)
        action(it)
    }
}

fun View.tapShort(action: (view: View?) -> Unit) {
    setOnClickListener {
        it.isEnabled = false
        it.postDelayed({ it.isEnabled = true }, 500)
        action(it)
    }
}

fun View.hideKeyboard() {
    val context = this.context ?: return
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.enable() {
    isEnabled = true
    isClickable = true
    alpha = 1f
}

fun View.disable() {
    isEnabled = false
    isClickable = false
    alpha = 0.4f
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun EditText.getTextEx(): String = text.toString().trim()

fun loadImage(view: ImageView, url: String) {
    Glide.with(view.context).load(url).into(view)
}

fun loadImage(
    imageView: ImageView,
    url: String,
    onShowLoading: (() -> Unit)? = null,
    onDismissLoading: (() -> Unit)? = null,
) {
    onShowLoading?.invoke()
    Glide.with(imageView.context)
        .load(url)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>,
                isFirstResource: Boolean,
            ): Boolean {
                onDismissLoading?.invoke()
                return false
            }

            override fun onResourceReady(
                resource: Drawable,
                model: Any,
                target: Target<Drawable>?,
                dataSource: DataSource,
                isFirstResource: Boolean,
            ): Boolean {
                onDismissLoading?.invoke()
                return false
            }
        })
        .into(imageView)
}


