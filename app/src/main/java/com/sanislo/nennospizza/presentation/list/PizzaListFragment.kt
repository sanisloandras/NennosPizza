package com.sanislo.nennospizza.presentation.list

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.sanislo.nennospizza.R
import com.sanislo.nennospizza.presentation.EventObserver
import com.sanislo.nennospizza.presentation.MainViewModel
import com.sanislo.nennospizza.presentation.details.PizzaDetailsFragment
import kotlinx.android.synthetic.main.fragment_pizza_list.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PizzaListFragment : Fragment(R.layout.fragment_pizza_list) {
    private val viewModel: MainViewModel by sharedViewModel()

    private val pizzaListAdapter = PizzaListAdapter(object : PizzaListAdapter.ClickHandler {
        override fun onClick(pizzaListItem: PizzaListItem, ivPizza: ImageView, adapterPosition: Int) {
            iv = ivPizza
            viewModel.onPizzaClick(pizzaListItem, adapterPosition)
        }
    })

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observePizzaList()
        observeNavigateToPizzaDetails()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        rv_pizza_list.adapter = pizzaListAdapter
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
        viewModel.pizzaList.observe(viewLifecycleOwner, Observer {
            pizzaListAdapter.submitList(it)
        })
    }

    private var iv: ImageView? = null

    private fun observeNavigateToPizzaDetails() {
        viewModel.navigateToPizzaDetailsEvent.observe(viewLifecycleOwner, EventObserver {
            //val ivPizza = (rv_pizza_list.findViewHolderForAdapterPosition(it.adapterPosition) as PizzaListAdapter.ViewHolder).ivPizza
            val ivPizza = iv!!
            val transitionName = ivPizza.transitionName
            requireActivity().supportFragmentManager
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .addSharedElement(ivPizza, transitionName)
                    .addToBackStack(null)
                    .replace(R.id.fl_fragment_container, PizzaDetailsFragment.newInstance(it))
                    .commit()
        })
    }
}