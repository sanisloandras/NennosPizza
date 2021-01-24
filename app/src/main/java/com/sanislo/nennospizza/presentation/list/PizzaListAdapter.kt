package com.sanislo.nennospizza.presentation.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sanislo.nennospizza.R
import com.sanislo.nennospizza.presentation.PizzaImageLoader
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PizzaListAdapter(val clickHandler: ClickHandler) : ListAdapter<PizzaListItem, PizzaListAdapter.ViewHolder>(
    asyncDifferConfig
), KoinComponent {
    val pizzaImageLoader: PizzaImageLoader by inject()

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<PizzaListItem>() {
            override fun areItemsTheSame(oldItem: PizzaListItem, newItem: PizzaListItem): Boolean {
                return oldItem.ingredientIds == newItem.ingredientIds
            }

            override fun areContentsTheSame(
                oldItem: PizzaListItem,
                newItem: PizzaListItem
            ): Boolean {
                return oldItem == newItem
            }

        }
        val asyncDifferConfig = AsyncDifferConfig.Builder(diffCallback).build()
    }

    interface ClickHandler {
        fun onClick(pizzaListItem: PizzaListItem, ivPizza: ImageView, adapterPosition: Int)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivWood = itemView.findViewById<ImageView>(R.id.iv_wood)
        val ivPizza = itemView.findViewById<ImageView>(R.id.iv_pizza)
        private val tvName = itemView.findViewById<TextView>(R.id.tv_name)
        private val tvIngredients = itemView.findViewById<TextView>(R.id.tv_ingredients)
        private val tvPrice = itemView.findViewById<TextView>(R.id.tv_price)

        init {
            itemView.setOnClickListener { clickHandler.onClick(getItem(adapterPosition), ivPizza, adapterPosition) }
        }

        fun bind() {
            ivPizza.transitionName = itemView.context.getString(R.string.pizza_details_transition, adapterPosition)
            getItem(adapterPosition).run {
                pizzaImageLoader.loadPizzaImage(ivWood, ivPizza, imgUrl)
                tvName.text = name
                tvIngredients.text = ingredients
                tvPrice.text = price
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_pizza, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }
}