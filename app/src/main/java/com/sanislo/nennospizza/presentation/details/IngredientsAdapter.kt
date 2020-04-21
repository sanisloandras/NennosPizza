package com.sanislo.nennospizza.presentation.details

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

class IngredientsAdapter(val clickHandler: ClickHandler) : ListAdapter<IngredientListItem, IngredientsAdapter.ViewHolder>(AsyncDifferConfig.Builder<IngredientListItem>(object :
        DiffUtil.ItemCallback<IngredientListItem>() {

        override fun areItemsTheSame(
            oldItem: IngredientListItem,
            newItem: IngredientListItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: IngredientListItem,
            newItem: IngredientListItem
        ): Boolean {
            return oldItem == newItem
        }

    }).build()) {

    var selection = mutableMapOf<Int, Boolean>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName = itemView.findViewById<TextView>(R.id.tv_name)
        private val tvPrice = itemView.findViewById<TextView>(R.id.tv_price)
        private val cb = itemView.findViewById<CheckBox>(R.id.cb)

        fun bind() {
            getItem(adapterPosition).run {
                tvName.text = name
                tvPrice.text = price
                cb.setOnCheckedChangeListener(null)
                cb.isChecked = selection[id] ?: false
                cb.setOnCheckedChangeListener { _, isChecked ->
                    selection[id] = isChecked
                    clickHandler.onSelectionChanged(id, isChecked)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_ingredient, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    interface ClickHandler {
        fun onSelectionChanged(ingredientId: Int, isSelected: Boolean)
    }
}