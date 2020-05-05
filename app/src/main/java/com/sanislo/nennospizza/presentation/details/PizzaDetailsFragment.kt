package com.sanislo.nennospizza.presentation.details

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.sanislo.nennospizza.R
import com.sanislo.nennospizza.presentation.PizzaImageLoader
import com.sanislo.nennospizza.setupToolbarForBack
import kotlinx.android.synthetic.main.fragment_pizza_details.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PizzaDetailsFragment : Fragment(R.layout.fragment_pizza_details) {
    private val viewModel: PizzaDetailsViewModel by viewModel()
    private val pizzaImageLoader: PizzaImageLoader by inject()
    private val ingredientsAdapter = IngredientsAdapter(object : IngredientsAdapter.ClickHandler {
        override fun onSelectionChanged(selection: Set<Int>) {
            viewModel.onSelectionChanged(selection)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        if (savedInstanceState == null) viewModel.setPizzaName(arguments?.getString(EXTRA_NAME))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbarForBack()
        rv_ingredients.adapter = ingredientsAdapter
        observePizzaDetails()
        observeIngredientList()
        add_to_cart.setOnClickListener { viewModel.addPizzaToCart() }
        observeCartState()
    }

    private fun observeCartState() {
        viewModel.addToCartState.observe(viewLifecycleOwner, {
            add_to_cart.isEnabled = it.isEnabled
            iv_cart.visibility = if (it.isEnabled) View.VISIBLE else View.GONE
            tv_add_to_cart.text = if (it.isEnabled) getString(R.string.add_to_cart, it.price)
            else getString(R.string.added_to_cart)
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

    private fun observePizzaDetails() {
        viewModel.pizzaDetails.observe(viewLifecycleOwner, {
            (activity as? AppCompatActivity)?.supportActionBar?.title = it.name
            pizzaImageLoader.loadPizzaImage(
                layout_pizza_image.findViewById(R.id.iv_wood),
                layout_pizza_image.findViewById(R.id.iv_pizza),
                it.imgUrl
            )
            //ingredientsAdapter.selection = it.initialSelection.toMutableSet()
            //ingredientsAdapter.submitList(it.ingredientList)
        })
    }

    private fun observeIngredientList() {
        viewModel.ingredientList.observe(viewLifecycleOwner, {
            ingredientsAdapter.submitList(it)
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EXTRA_NAME, arguments?.getString(EXTRA_NAME))
        outState.putIntArray(EXTRA_SELECTION, ingredientsAdapter.selection.toIntArray())
        super.onSaveInstanceState(outState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        savedInstanceState?.let {
            viewModel.setPizzaName(savedInstanceState.getString(EXTRA_NAME))
            val selection = savedInstanceState.getIntArray(EXTRA_SELECTION)?.toSet() ?: emptySet()
            ingredientsAdapter.selection = selection.toMutableSet()
            viewModel.onSelectionChanged(selection)
        }
    }

    companion object {
        const val EXTRA_NAME = "EXTRA_NAME"
        const val EXTRA_SELECTION = "EXTRA_SELECTION"

        fun newInstance(pizzaName: String): PizzaDetailsFragment {
            return PizzaDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_NAME, pizzaName)
                }
            }
        }
    }
}