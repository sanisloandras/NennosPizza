package com.sanislo.nennospizza.presentation.drinks

import android.os.Bundle
import android.util.Log.d
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.sanislo.nennospizza.R
import com.sanislo.nennospizza.setupToolbarForBack
import kotlinx.android.synthetic.main.fragment_drinks.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DrinkListFragment : Fragment(R.layout.fragment_drinks) {
    private val viewModel: DrinkListViewModel by viewModel()

    private val drinkListAdapter = DrinkListAdapter(object : DrinkListAdapter.ClickHandler {
        override fun add(drinkListItem: DrinkListItem) {
            viewModel.addDrink(drinkListItem)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbarForBack(toolbar, getString(R.string.drinks))
        rv_drinks.adapter = drinkListAdapter
        observeDrinks()
        observeAddedToCart()
    }

    private fun observeAddedToCart() {
        viewModel.drinkAddedToCart.observe(viewLifecycleOwner, {
            tv_added_to_cart.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    private fun observeDrinks() {
        viewModel.drinks.observe(viewLifecycleOwner, {
            drinkListAdapter.submitList(it)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}