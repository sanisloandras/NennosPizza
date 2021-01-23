package com.sanislo.nennospizza.presentation.cart

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.sanislo.nennospizza.R
import com.sanislo.nennospizza.presentation.EventObserver
import com.sanislo.nennospizza.presentation.cart.data.BaseCartItem
import com.sanislo.nennospizza.presentation.drinks.DrinkListFragment
import com.sanislo.nennospizza.presentation.thankyou.ThankYouFragment
import com.sanislo.nennospizza.setupToolbarForBack
import kotlinx.android.synthetic.main.fragment_cart.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartFragment : Fragment(R.layout.fragment_cart) {
    private val viewModel: CartViewModel by viewModel()
    private val onItemClickListener: (BaseCartItem) -> Unit = {
        viewModel.onRemoveCartItem(it)
    }
    private val cartListAdapter = CartListAdapter(onItemClickListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbarForBack(toolbar, getString(R.string.cart))
        rv_cart.adapter = cartListAdapter
        tv_checkout.setOnClickListener { viewModel.checkout() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeCart()
        observeNavigateToDrinks()
        observeErrors()
        observeNavigateToThankYou()
    }

    private fun observeErrors() {
        viewModel.errors.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(requireContext(), it.message
                    ?: getString(R.string.unexpected_error), Toast.LENGTH_LONG).show()
        })
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
        else -> super.onOptionsItemSelected(item)
    }

    private fun observeCart() {
        viewModel.cart.observe(viewLifecycleOwner, Observer {
            cartListAdapter.submitList(it.cartItems)
            tv_checkout.text = getString(R.string.checkout, it.price)
            tv_checkout.visibility = if (it.cartItems.isEmpty()) View.GONE else View.VISIBLE
            tv_empty_cart.visibility = if (it.cartItems.isEmpty()) View.VISIBLE else View.GONE
        })
    }

    private fun observeNavigateToDrinks() {
        viewModel.navigateToDrinksEvent.observe(viewLifecycleOwner, EventObserver {
            requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_fragment_container, DrinkListFragment())
                    .addToBackStack(null)
                    .commit()
        })
    }

    private fun observeNavigateToThankYou() {
        viewModel.navigateToThankYouEvent.observe(viewLifecycleOwner, EventObserver {
            requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_fragment_container, ThankYouFragment())
                    .commit()
        })
    }
}