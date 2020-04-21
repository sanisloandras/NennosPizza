package com.sanislo.nennospizza.presentation

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sanislo.nennospizza.R

interface PizzaImageLoader {
    fun loadPizzaImage(
                       ivWood: ImageView,
                       ivPizza: ImageView,
                       pizzaImgUrl: String?)
}

class PizzaImageLoaderImpl(private val context: Context) : PizzaImageLoader {
    override fun loadPizzaImage(
        ivWood: ImageView,
        ivPizza: ImageView,
        pizzaImgUrl: String?
    ) {
        Glide.with(context).load(R.drawable.bg_wood).into(ivWood)
        Glide.with(context).load(pizzaImgUrl)
            .apply(RequestOptions().placeholder(R.drawable.custom).error(R.drawable.custom))
            .into(ivPizza)
    }
}