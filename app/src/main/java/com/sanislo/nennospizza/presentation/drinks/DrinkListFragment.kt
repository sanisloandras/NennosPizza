package com.sanislo.nennospizza.presentation.drinks

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.sanislo.nennospizza.R
import com.sanislo.nennospizza.presentation.MainViewModel
import com.sanislo.nennospizza.setupToolbarForBack
import kotlinx.android.synthetic.main.fragment_drinks.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DrinkListFragment : Fragment(R.layout.fragment_drinks) {
    private val viewModel: MainViewModel by sharedViewModel()
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
        setupToolbarForBack(getString(R.string.drinks))
        rv_drinks.adapter = drinkListAdapter
        viewModel.loadDrinks()
        observeDrinks()
        observeAddedToCart()
    }

    private fun observeAddedToCart() {
        viewModel.drinkAddedToCart.observe(viewLifecycleOwner, Observer {
            tv_added_to_cart.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    private fun observeDrinks() {
        viewModel.drinks.observe(viewLifecycleOwner, Observer {
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