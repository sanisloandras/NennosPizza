package com.sanislo.nennospizza.presentation

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.sanislo.nennospizza.R

interface PizzaImageLoader {
    fun loadPizzaImage(
            ivWood: ImageView,
            ivPizza: ImageView,
            pizzaImgUrl: String?,
            requestListener: RequestListener<Drawable>? = null)
}

class PizzaImageLoaderImpl(private val context: Context) : PizzaImageLoader {
    override fun loadPizzaImage(
            ivWood: ImageView,
            ivPizza: ImageView,
            pizzaImgUrl: String?,
            requestListener: RequestListener<Drawable>?
    ) {
        Glide.with(context).load(R.drawable.bg_wood).into(ivWood)
        val builder = Glide.with(context).load(pizzaImgUrl)
            .apply(RequestOptions().placeholder(R.drawable.custom).error(R.drawable.custom))
        if (requestListener != null) builder.listener(requestListener)
        builder.into(ivPizza)
    }
}