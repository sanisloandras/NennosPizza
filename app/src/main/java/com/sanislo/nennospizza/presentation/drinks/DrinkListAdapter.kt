package com.sanislo.nennospizza.presentation.drinks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sanislo.nennospizza.R

class DrinkListAdapter(private val clickHandler: ClickHandler) : ListAdapter<DrinkListItem, DrinkListAdapter.ViewHolder>(AsyncDifferConfig.Builder<DrinkListItem>(object : DiffUtil.ItemCallback<DrinkListItem>() {
        override fun areItemsTheSame(oldItem: DrinkListItem, newItem: DrinkListItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DrinkListItem, newItem: DrinkListItem): Boolean {
            return oldItem == newItem
        }

    }).build()) {

    interface ClickHandler {
        fun add(drinkListItem: DrinkListItem)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName = itemView.findViewById<TextView>(R.id.tv_name)
        private val tvPrice = itemView.findViewById<TextView>(R.id.tv_price)
        private val ibAdd = itemView.findViewById<ImageButton>(R.id.ib_add)

        init {
            ibAdd.setOnClickListener {
                clickHandler.add(getItem(adapterPosition))
            }
        }

        fun bind() {
            getItem(adapterPosition).run {
                tvName.text = name
                tvPrice.text = price
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_drink, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }
}