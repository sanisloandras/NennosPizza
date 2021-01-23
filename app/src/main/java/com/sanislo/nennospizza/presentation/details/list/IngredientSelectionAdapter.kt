package com.sanislo.nennospizza.presentation.details.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sanislo.nennospizza.R

class IngredientSelectionAdapter(val clickHandler: ClickHandler)
    : ListAdapter<IngredientItem, IngredientSelectionAdapter.IngredientViewHolder>(asyncDifferConfig) {
    companion object {
        private val diffUtilItemCallback = object : DiffUtil.ItemCallback<IngredientItem>() {
            override fun areItemsTheSame(oldItem: IngredientItem, newItem: IngredientItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: IngredientItem, newItem: IngredientItem): Boolean {
                return oldItem == newItem
            }

        }
        val asyncDifferConfig = AsyncDifferConfig.Builder(diffUtilItemCallback).build()
    }

    //todo try selectionadapter
    var selection = mutableSetOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        return IngredientViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_ingredient, parent, false))
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName = itemView.findViewById<TextView>(R.id.tv_name)
        private val tvPrice = itemView.findViewById<TextView>(R.id.tv_price)
        private val cb = itemView.findViewById<CheckBox>(R.id.cb)

        fun bind(ingredientItem: IngredientItem) {
            ingredientItem.run {
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