package com.sanislo.nennospizza.presentation.details.list

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.sanislo.nennospizza.R
import com.sanislo.nennospizza.presentation.PizzaImageLoader

class PizzaDetailsAdapter(val clickHandler: PizzaDetailsAdapter.ClickHandler,
                          val pizzaImageLoadedCallback: PizzaImageLoadedCallback,
                          val pizzaImageLoader: PizzaImageLoader) : ListAdapter<BasePizzaDetailsItem, PizzaDetailsAdapter.BaseViewHolder>(asyncDifferConfig) {
    companion object {
        private val diffUtilItemCallback = object : DiffUtil.ItemCallback<BasePizzaDetailsItem>() {
            override fun areItemsTheSame(oldItem: BasePizzaDetailsItem, newItem: BasePizzaDetailsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: BasePizzaDetailsItem, newItem: BasePizzaDetailsItem): Boolean {
                return oldItem == newItem
            }

        }
        val asyncDifferConfig = AsyncDifferConfig.Builder(diffUtilItemCallback).build()
    }

    var selection = mutableSetOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            R.layout.layout_pizza_image -> PizzaDetailsViewHolder(getViewByViewType(parent, viewType))
            R.layout.item_ingredient -> IngredientViewHolder(getViewByViewType(parent, viewType))
            else -> throw IllegalArgumentException("Can not return ViewHolder for viewType $viewType")
        }
    }

    private fun getViewByViewType(parent: ViewGroup, viewType: Int) =
            LayoutInflater.from(parent.context).inflate(viewType, parent, false)

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is PizzaImageItem -> R.layout.layout_pizza_image
            is IngredientItem -> R.layout.item_ingredient
            else -> super.getItemViewType(position)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind()
    }

    abstract inner class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        open fun bind() {}
    }

    inner class PizzaDetailsViewHolder(itemView: View) : BaseViewHolder(itemView) {
        private val ivPizza: ImageView = itemView.findViewById(R.id.iv_pizza)
        private val ivWood: ImageView = itemView.findViewById(R.id.iv_wood)

        override fun bind() {
            super.bind()
            ivPizza.transitionName = itemView.context.getString(R.string.pizza_details_transition, 1)
            (getItem(adapterPosition) as PizzaImageItem).run {
                pizzaImageLoader.loadPizzaImage(
                        ivWood,
                        ivPizza,
                        imageUrl,
                        object : RequestListener<Drawable> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                TODO("Not yet implemented")
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

    inner class IngredientViewHolder(itemView: View) : BaseViewHolder(itemView) {
        private val tvName = itemView.findViewById<TextView>(R.id.tv_name)
        private val tvPrice = itemView.findViewById<TextView>(R.id.tv_price)
        private val cb = itemView.findViewById<CheckBox>(R.id.cb)

        override fun bind() {
            (getItem(adapterPosition) as IngredientItem).run {
                tvName.text = name
                tvPrice.text = price
                cb.setOnCheckedChangeListener(null)
                cb.isChecked = selection.contains(id)
                cb.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) selection.add(id) else selection.remove(id)
                    clickHandler.onSelectionChanged(id, isChecked)
                }
            }
        }
    }

    interface ClickHandler {
        fun onSelectionChanged(id: Int, isSelected: Boolean)
    }

    interface PizzaImageLoadedCallback {
        fun pizzaImageLoaded()
    }
}