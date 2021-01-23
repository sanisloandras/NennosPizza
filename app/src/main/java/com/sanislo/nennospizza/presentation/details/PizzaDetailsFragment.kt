package com.sanislo.nennospizza.presentation.details

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ConcatAdapter
import com.sanislo.nennospizza.R
import com.sanislo.nennospizza.presentation.PizzaImageLoader
import com.sanislo.nennospizza.presentation.details.list.IngredientSelectionAdapter
import com.sanislo.nennospizza.setupToolbarForBack
import kotlinx.android.synthetic.main.fragment_pizza_details.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PizzaDetailsFragment : Fragment(R.layout.fragment_pizza_details) {
    private val viewModel: PizzaDetailsViewModel by viewModel()
    private val pizzaImageLoader: PizzaImageLoader by inject()

    private lateinit var pizzaDetailsInput: PizzaDetailsInput

    private val pizzaImageAdapter = PizzaDetailsHeaderAdapter(pizzaImageLoader, object : IngredientSelectionAdapter.PizzaImageLoadedCallback {
        override fun pizzaImageLoaded() {
            startPostponedEnterTransition()
        }
    })
    private val pizzaDetailsAdapter = IngredientSelectionAdapter(object : IngredientSelectionAdapter.ClickHandler {
        override fun onSelectionChanged(id: Int, isSelected: Boolean) {
            viewModel.onSelectionChanged(id, isSelected)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(R.transition.image_shared_element_transition)
        // Avoid a postponeEnterTransition on orientation change, and postpone only of first creation.
        if (savedInstanceState == null) postponeEnterTransition()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbarForBack(toolbar)
        val concatAdapter = ConcatAdapter(
                pizzaImageAdapter,
                pizzaDetailsAdapter
        )
        rv.adapter = concatAdapter
        observePizzaDetailsState()
        observeCartState()
        observePizzaName()
        add_to_cart.setOnClickListener { viewModel.addPizzaToCart(pizzaDetailsAdapter.selection) }
    }

    private fun observePizzaName() {
        viewModel.pizzaName.observe(viewLifecycleOwner, Observer {
            toolbar.title = it
        })
    }

    private fun observePizzaDetailsState() {
        viewModel.pizzaDetailsState.observe(viewLifecycleOwner, Observer {
            pizzaImageAdapter.submitList(listOf(it.header))
            pizzaDetailsAdapter.selection = it.selection.toMutableSet()
            pizzaDetailsAdapter.submitList(it.ingredients)
        })
    }

    private fun observeCartState() {
        viewModel.addToCartState.observe(viewLifecycleOwner, Observer {
            if (!it.isLoaded) {
                add_to_cart.isEnabled = false
                tv_add_to_cart.text = getString(R.string.add_to_cart, it.price)
                return@Observer
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

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(EXTRA_INPUT, pizzaDetailsInput)
        outState.putIntArray(EXTRA_SELECTION, pizzaDetailsAdapter.selection.toIntArray())
        super.onSaveInstanceState(outState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState == null) {
            pizzaDetailsInput = arguments?.getParcelable(EXTRA_INPUT)!!
            viewModel.pizzaDetailsInput.value = pizzaDetailsInput
        } else {
            pizzaDetailsInput = savedInstanceState.getParcelable(EXTRA_INPUT)!!
            val selection = savedInstanceState.getIntArray(EXTRA_SELECTION)?.toSet()
            viewModel.restoreState(pizzaDetailsInput, selection)
        }
    }

    companion object {
        const val EXTRA_INPUT = "EXTRA_INPUT"
        const val EXTRA_SELECTION = "EXTRA_SELECTION"

        val TAG = PizzaDetailsFragment::class.java.simpleName

        fun newInstance(pizzaDetailsInput: PizzaDetailsInput): PizzaDetailsFragment {
            return PizzaDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_INPUT, pizzaDetailsInput)
                }
            }
        }
    }
}