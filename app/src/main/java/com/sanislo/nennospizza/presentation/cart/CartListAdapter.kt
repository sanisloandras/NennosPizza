package com.sanislo.nennospizza.presentation.cart

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
import com.sanislo.nennospizza.presentation.cart.data.BaseCartItem

class CartListAdapter(private val itemClickListener: (BaseCartItem) -> Unit) :
    ListAdapter<BaseCartItem, CartListAdapter.ViewHolder>(AsyncDifferConfig.Builder<BaseCartItem>(object : DiffUtil.ItemCallback<BaseCartItem>() {
        override fun areItemsTheSame(oldItem: BaseCartItem, newItem: BaseCartItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BaseCartItem, newItem: BaseCartItem): Boolean {
            return oldItem == newItem
        }

    }).build()) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName = itemView.findViewById<TextView>(R.id.tv_name)
        private val tvPrice = itemView.findViewById<TextView>(R.id.tv_price)
        private val ibDelete = itemView.findViewById<ImageButton>(R.id.ib_delete)

        init {
            ibDelete.setOnClickListener {
                if (adapterPosition == -1) return@setOnClickListener
                itemClickListener(getItem(adapterPosition))
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
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }
}