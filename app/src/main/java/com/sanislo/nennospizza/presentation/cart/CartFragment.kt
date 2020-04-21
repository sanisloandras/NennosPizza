package com.sanislo.nennospizza.presentation.cart

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.sanislo.nennospizza.R
import com.sanislo.nennospizza.presentation.MainViewModel
import com.sanislo.nennospizza.presentation.cart.data.BaseCartItem
import com.sanislo.nennospizza.setupToolbarForBack
import kotlinx.android.synthetic.main.fragment_cart.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CartFragment : Fragment(R.layout.fragment_cart) {
    private val viewModel: MainViewModel by sharedViewModel()
    private val cartListAdapter = CartListAdapter(object : CartListAdapter.ClickHandler {
        override fun onRemove(baseCartItem: BaseCartItem) {
            viewModel.onRemoveCartItem(baseCartItem)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbarForBack(getString(R.string.cart))
        rv_cart.adapter = cartListAdapter
        observeCart()
        tv_checkout.setOnClickListener { viewModel.checkout() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_cart, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            activity?.onBackPressed()
            true
        }
        R.id.action_drinks -> {
            viewModel.onTapDrinks()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun observeCart() {
        viewModel.cart.observe(viewLifecycleOwner, Observer {
            cartListAdapter.submitList(it.cartItems)
            tv_checkout.text = getString(R.string.checkout, it.price)
            tv_checkout.visibility = if (it.cartItems.isEmpty()) View.GONE else View.VISIBLE
            tv_empty_cart.visibility = if (it.cartItems.isEmpty()) View.VISIBLE else View.GONE
        })
    }
}