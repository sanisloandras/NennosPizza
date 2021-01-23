package com.sanislo.nennospizza.presentation.details

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.sanislo.nennospizza.R
import com.sanislo.nennospizza.presentation.PizzaImageLoader
import com.sanislo.nennospizza.presentation.details.list.IngredientSelectionAdapter
import com.sanislo.nennospizza.presentation.details.list.PizzaDetailsHeader

class PizzaDetailsHeaderAdapter(val pizzaImageLoader: PizzaImageLoader,
                                val pizzaImageLoadedCallback: IngredientSelectionAdapter.PizzaImageLoadedCallback)
    : ListAdapter<PizzaDetailsHeader, PizzaDetailsHeaderAdapter.PizzaDetailsHeaderViewHolder>(asyncDifferConfig) {

    companion object {
        private val diffUtilItemCallback = object : DiffUtil.ItemCallback<PizzaDetailsHeader>() {
            override fun areItemsTheSame(oldItem: PizzaDetailsHeader, newItem: PizzaDetailsHeader): Boolean {
                return oldItem.imageUrl.equals(newItem.imageUrl)
            }

            override fun areContentsTheSame(oldItem: PizzaDetailsHeader, newItem: PizzaDetailsHeader): Boolean {
                return oldItem == newItem
            }
        }
        val asyncDifferConfig = AsyncDifferConfig.Builder(diffUtilItemCallback).build()
    }

    inner class PizzaDetailsHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivPizza: ImageView = itemView.findViewById(R.id.iv_pizza)
        private val ivWood: ImageView = itemView.findViewById(R.id.iv_wood)

        fun bind(item: PizzaDetailsHeader) {
            item.run {
                ivPizza.transitionName = transitionName
                pizzaImageLoader.loadPizzaImage(
                        ivWood,
                        ivPizza,
                        imageUrl,
                        object : RequestListener<Drawable> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                pizzaImageLoadedCallback.pizzaImageLoaded()
                                return false
                            }

                            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: com.bumptech.glide.load.DataSource?, isFirstResource: Boolean): Boolean {
                                pizzaImageLoadedCallback.pizzaImageLoaded()
                                return false
                            }

                        }
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PizzaDetailsHeaderViewHolder {
        return PizzaDetailsHeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_pizza_image, parent, false))
    }

    override fun onBindViewHolder(holder: PizzaDetailsHeaderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}