package com.sanislo.nennospizza.presentation.details

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.transition.TransitionInflater
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.sanislo.nennospizza.R
import com.sanislo.nennospizza.presentation.PizzaImageLoader
import com.sanislo.nennospizza.setupToolbarForBack
import kotlinx.android.synthetic.main.fragment_pizza_details.*
import kotlinx.android.synthetic.main.layout_pizza_image.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PizzaDetailsFragment : Fragment(R.layout.fragment_pizza_details) {
    private val viewModel: PizzaDetailsViewModel by viewModel()
    private val pizzaImageLoader: PizzaImageLoader by inject()
    private lateinit var pizzaDetailsInput: PizzaDetailsInput
    private val ingredientsAdapter = IngredientsAdapter(object : IngredientsAdapter.ClickHandler {

        override fun onSelectionChanged(id: Int, isSelected: Boolean) {
            viewModel.onSelectionChanged(id, isSelected)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        sharedElementEnterTransition = TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move)
        pizzaDetailsInput = arguments?.getParcelable(EXTRA_INPUT)!!
        //if (savedInstanceState == null) viewModel.setPizzaName(arguments?.getString(EXTRA_NAME))
        if (savedInstanceState == null) viewModel.setPizzaName(pizzaDetailsInput.pizzaName)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        postponeEnterTransition()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbarForBack()
        iv_pizza.transitionName = getString(R.string.pizza_details_transition, pizzaDetailsInput.adapterPosition)

        rv_ingredients.adapter = ingredientsAdapter
        observePizzaDetails()
        observeIngredientListState()
        observeCartState()
        add_to_cart.setOnClickListener { viewModel.addPizzaToCart(ingredientsAdapter.selection) }
    }

    private fun observeCartState() {
        viewModel.addToCartState.observe(viewLifecycleOwner, {
            if (!it.isLoaded) {
                add_to_cart.isEnabled = false
                tv_add_to_cart.text = getString(R.string.add_to_cart, it.price)
                return@observe
            }
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
                    it.imgUrl, object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    TODO("Not yet implemented")
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

            }
            )
        })
    }

    private fun observeIngredientListState() {
        viewModel.ingredientListState.observe(viewLifecycleOwner, {
            ingredientsAdapter.selection = it.selection.toMutableSet()
            ingredientsAdapter.submitList(it.ingredientList)
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
            val pizzaName = savedInstanceState.getString(EXTRA_NAME)
            val selection = savedInstanceState.getIntArray(EXTRA_SELECTION)?.toSet() ?: emptySet()
            viewModel.restoreState(pizzaName, selection)
        }
    }

    companion object {
        const val EXTRA_NAME = "EXTRA_NAME"
        const val EXTRA_INPUT = "EXTRA_INPUT"
        const val EXTRA_SELECTION = "EXTRA_SELECTION"

        /*fun newInstance(pizzaName: String): PizzaDetailsFragment {
            return PizzaDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_NAME, pizzaName)
                }
            }
        }*/

        fun newInstance(pizzaDetailsInput: PizzaDetailsInput): PizzaDetailsFragment {
            return PizzaDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_INPUT, pizzaDetailsInput)
                }
            }
        }
    }
}