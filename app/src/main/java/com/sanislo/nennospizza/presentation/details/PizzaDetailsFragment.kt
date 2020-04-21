package com.sanislo.nennospizza.presentation.details

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.sanislo.nennospizza.R
import com.sanislo.nennospizza.presentation.MainViewModel
import com.sanislo.nennospizza.presentation.PizzaImageLoader
import com.sanislo.nennospizza.setupToolbarForBack
import kotlinx.android.synthetic.main.fragment_pizza_details.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PizzaDetailsFragment : Fragment(R.layout.fragment_pizza_details) {
    private val viewModel: MainViewModel by sharedViewModel()
    private val pizzaImageLoader: PizzaImageLoader by inject()
    private val ingredientsAdapter = IngredientsAdapter(object : IngredientsAdapter.ClickHandler {
        override fun onSelectionChanged(ingredientId: Int, isSelected: Boolean) {
            viewModel.onIngredientSelected(ingredientId, isSelected)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbarForBack()
        rv_ingredients.adapter = ingredientsAdapter
        observePizzaDetails()
        add_to_cart.setOnClickListener {
            viewModel.addPizzaToCart(ingredientsAdapter.selection)
        }
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

    private fun observePizzaDetails() {
        viewModel.pizzaDetails.observe(viewLifecycleOwner, Observer {
            (activity as? AppCompatActivity)?.supportActionBar?.title = it.name
            pizzaImageLoader.loadPizzaImage(
                layout_pizza_image.findViewById(R.id.iv_wood),
                layout_pizza_image.findViewById(R.id.iv_pizza),
                it.imgUrl
            )
            ingredientsAdapter.selection = it.initialSelection.toMutableMap()
            ingredientsAdapter.submitList(it.ingredientList)
            add_to_cart.isEnabled = it.isAddToCartEnabled
            iv_cart.visibility = if (it.isAddToCartEnabled) View.VISIBLE else View.GONE
            tv_add_to_cart.text =
                if (it.isAddToCartEnabled) getString(R.string.add_to_cart, it.price) else getString(
                    R.string.added_to_cart
                )
        })
    }
}