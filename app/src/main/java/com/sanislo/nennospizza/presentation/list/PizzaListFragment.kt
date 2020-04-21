package com.sanislo.nennospizza.presentation.list

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.sanislo.nennospizza.R
import com.sanislo.nennospizza.presentation.MainViewModel
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_pizza_list.*
import kotlinx.android.synthetic.main.fragment_pizza_list.toolbar
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PizzaListFragment : Fragment(R.layout.fragment_pizza_list) {
    private val viewModel: MainViewModel by sharedViewModel()
    private val pizzaListAdapter =
        PizzaListAdapter(object :
            PizzaListAdapter.ClickHandler {
            override fun onClick(pizzaListItem: PizzaListItem) {
                viewModel.onPizzaClick(pizzaListItem)
            }
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        rv_pizza_list.adapter = pizzaListAdapter
        observePizzaList()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_cart -> {
            viewModel.onTapCart()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun observePizzaList() {
        viewModel.pizzaList.observe(viewLifecycleOwner, Observer<List<PizzaListItem>> {
            pizzaListAdapter.submitList(it)
        })
    }
}